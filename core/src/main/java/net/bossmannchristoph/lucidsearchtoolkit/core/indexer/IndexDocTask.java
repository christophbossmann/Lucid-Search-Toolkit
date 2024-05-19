package net.bossmannchristoph.lucidsearchtoolkit.core.indexer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;

import net.bossmannchristoph.lucidsearchtoolkit.core.TechnicalException;
import net.bossmannchristoph.lucidsearchtoolkit.core.Utilities;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.exception.ZeroByteFileException;

public class IndexDocTask implements Runnable {

	public Collection<String> indexedFileTypes;
	public PrintStream out;
	public Tika tika;
	public Map<String, Long> fileMap;
	public IndexWriter writer;

	private Path file;
	private Path docDir;
	private Path relativeFilePath;
	private String indexedFileString;

	public static final Logger LOGGER = LogManager.getLogger(IndexDocTask.class.getName());

	private long lastModified;

	private IndexDocTask(Collection<String> indexedFileTypes, PrintStream out, Tika tika, Map<String, Long> fileMap,
			IndexWriter writer) {
		this.indexedFileTypes = indexedFileTypes;
		this.out = out;
		this.tika = tika;
		this.fileMap = fileMap;
		this.writer = writer;
	}

	public static IndexDocTask getTaskInstance(Collection<String> indexedFileTypes, PrintStream out, Tika tika,
			Map<String, Long> fileMap, IndexWriter writer) {
		return new IndexDocTask(indexedFileTypes, out, tika, fileMap, writer);
	}

	public String init(Path file, long lastModified, Path docDir) {
		this.file = file;
		this.lastModified = lastModified;
		this.docDir = docDir;
		relativeFilePath = Utilities.getDocPath(docDir, file.toFile());
		indexedFileString = relativeFilePath.toString().toLowerCase();
		return indexedFileString;
	}

	@Override
	public void run() {
		try {
			indexDoc();
		} catch (IOException ioe) {
			LOGGER.log(Level.WARN, "IOException at indexDocTask: " + ioe.getMessage(), ioe);
		} catch (TikaException te) {
			LOGGER.log(Level.WARN, "TikaException at indexDocTask: " + te.getMessage(), te);
		} catch (Exception e) {
			throw new TechnicalException(e);
		}
	}

	private void indexDoc() throws IOException, TikaException {
		Long lastModifiedInIndex = fileMap.get(indexedFileString);
		if (lastModifiedInIndex != null && lastModifiedInIndex >= lastModified) {
			LuceneIndexerWithTika.printIndexMessage(LuceneIndexerWithTika.MESSAGE_NOT_CHANGED, indexedFileString, out);
			return;
		}
		if (!indexedFileTypes.contains(FilenameUtils.getExtension(indexedFileString))) {
			LuceneIndexerWithTika.printIndexMessage(LuceneIndexerWithTika.MESSAGE_IGNORED_FILETYPE, indexedFileString, out);
			return;
		}

		InputStream stream = Files.newInputStream(file);

		// Create lucene Document
		Document doc = new Document();

		try {
			// Updates a document by first deleting the document(s)
			// containing <code>term</code> and then adding the new
			// document. The delete and then add are atomic as seen
			// by a reader on the same index

			Long previousValue = fileMap.put(indexedFileString, lastModified);
			if (previousValue == null) {
				LuceneIndexerWithTika.printIndexMessage(LuceneIndexerWithTika.MESSAGE_CREATED, indexedFileString, out);
			} else {
				LuceneIndexerWithTika.printIndexMessage(LuceneIndexerWithTika.MESSAGE_MODIFIED, indexedFileString, out);
			}
			int availableBefore = stream.available();
			String extractedContent = tika.parseToString(stream);
			LOGGER.debug("File path: " + file.toString() +
					"\nStream (before) available: " + availableBefore +
					"\nExtracted Content (bytes): " + extractedContent.getBytes().length);
			stream.close();

			doc.add(new StringField("filename",
					file.toFile().getName(), Field.Store.YES));
			doc.add(new StringField("extension",
					FilenameUtils.getExtension(file.toFile().getName()), Field.Store.YES));
			doc.add(new StringField("path", relativeFilePath.toString(), Field.Store.YES));
			doc.add(new LongPoint("modified", lastModified));
			doc.add(new StoredField("modified", lastModified));
			doc.add(new TextField("contents", extractedContent, Store.YES));

			AtomicInteger index = new AtomicInteger(-1);
			StringJoiner sjPathElements = new StringJoiner(" ");
			String fileNameElements = "";
			Iterator<Path> iter = relativeFilePath.iterator();
			while (iter.hasNext()) {
				String pathElement = iter.next().toString().toLowerCase();
				//REPLACED WITH pathElements and fileNameElements
				//int nrPathElement = index.incrementAndGet();
				//doc.add(new StringField("path_" + nrPathElement, pathElement, Field.Store.YES));
				if(!iter.hasNext()) {
					fileNameElements = FilenameUtils.removeExtension(pathElement).toLowerCase();
				}
				else {
					sjPathElements.add(pathElement);
				}
			}
			doc.add(new TextField("pathelements", sjPathElements.toString(), Store.YES));
			doc.add(new TextField("filenameelements", fileNameElements, Store.YES));
			writer.updateDocument(new Term("path", indexedFileString), doc);

		} catch (ZeroByteFileException e) {
			LuceneIndexerWithTika.printIndexMessage(LuceneIndexerWithTika.MESSAGE_EMPTY_FILE_IGNORED, indexedFileString, out);
		}
	}

}
