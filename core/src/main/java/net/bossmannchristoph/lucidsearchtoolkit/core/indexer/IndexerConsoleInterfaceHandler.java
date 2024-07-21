package net.bossmannchristoph.lucidsearchtoolkit.core.indexer;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import net.bossmannchristoph.lucidsearchtoolkit.core.IConsoleHandler;
import net.bossmannchristoph.lucidsearchtoolkit.core.TechnicalException;
import org.apache.commons.cli.Option;

public class IndexerConsoleInterfaceHandler implements IConsoleHandler {

    private LuceneIndexerWithTika luceneIndexerWithTika;
    private Scanner sc;
    
    public static final String OUTPATH = "outpath";
    public static final String DOCSPATH = "docspath";
    public static final String INDEXPATH = "indexpath";
    public static final String INDEX_WITH_CONTENT_FILE_TYPES = "indexwithcontentfiletypes";

	public static final String IGNORE_FILE_TYPES = "ignorefiletypes";
    
    
	public static final String MODE_INDEX = "index";
	
	public static final String defaultIndexedWithContentFileTypes = "txt,pdf,docx,doc,xlsx,xls,ppt,pptx,odt,ods,odp";

	public static final String defaultIgnoreFileTypes = "dat,exe,dll,class";
	
    public IndexerConsoleInterfaceHandler(Scanner sc) {
    	luceneIndexerWithTika = new LuceneIndexerWithTika();
    	this.sc = sc;
    }
    
    @Override
	public void execute(Map<String, String> args) {
    	try {
    		askForParams(args);
        	luceneIndexerWithTika.prepareOutputAndLogging(args.get(OUTPATH));
        	luceneIndexerWithTika.prepare(args.get(DOCSPATH), args.get(INDEXPATH),
					args.getOrDefault(INDEX_WITH_CONTENT_FILE_TYPES, defaultIndexedWithContentFileTypes),
					args.getOrDefault(IGNORE_FILE_TYPES, defaultIgnoreFileTypes));
        	luceneIndexerWithTika.indexDocs();
    		luceneIndexerWithTika.close();
    	}
    	catch(IOException | ClassNotFoundException e) {
    		throw new TechnicalException(e);
    	}		
	}
	
	@Override
	public Option[] getOptions() {
			Option docsOpt = Option.builder("docs").longOpt(DOCSPATH).argName(DOCSPATH).hasArg().required(false)
					.desc("path of docs to be indexed").build();
	        Option indOpt = Option.builder("ind").longOpt(INDEXPATH).argName(INDEXPATH).hasArg().required(false)
					.desc("path where index of indexed docs shall be stored").build();
	        Option outOpt = Option.builder("out").longOpt(OUTPATH).argName(OUTPATH).hasArg().required(false)
					.desc("path where the result of the index process shall be stored").build();
			Option indexWithContentFileTypesOpt = Option.builder("iwcft").longOpt(INDEX_WITH_CONTENT_FILE_TYPES).argName(INDEX_WITH_CONTENT_FILE_TYPES).hasArg().required(false)
					.desc("types of files (extensions) to be indexed with content, seperated by comma").build();
			Option ignoreFileTypesOpt = Option.builder("ift").longOpt(IGNORE_FILE_TYPES).
					argName(IGNORE_FILE_TYPES).hasArg().required(false).
					desc("types of files (extensions) to be ignored during indexing").build();

	        docsOpt.setRequired(false);
	        indOpt.setRequired(false);
	        outOpt.setRequired(false);
	        return new Option[]{docsOpt, indOpt, outOpt, indexWithContentFileTypesOpt, ignoreFileTypesOpt};
	}
	
	private void askForParams(Map<String, String> args) {	
		if(args.get(DOCSPATH) == null) {			
			System.out.println("Enter document path: ");
			String param = sc.nextLine();
			args.put(DOCSPATH, param);			
		}
		if(args.get(INDEXPATH) == null) {
			System.out.println("Enter index path: ");
			String param = sc.nextLine();
			args.put(INDEXPATH, param);	
		}
		if(args.get(OUTPATH) == null) {
			System.out.println("Enter out path (or leave empty): ");
			String param = sc.nextLine();
			args.put(OUTPATH, param);		
		}
	}

	@Override
	public String getMode() {
		return MODE_INDEX;
	}
	
}
