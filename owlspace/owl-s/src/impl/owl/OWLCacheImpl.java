//The MIT License
//
// Copyright (c) 2004 Evren Sirin
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to
// deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
// sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

/*
 * Created on Dec 30, 2003
 *
 */
package impl.owl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import org.mindswap.owl.OWLCache;
import org.xml.sax.InputSource;

/**
 * 
 * 
 * 
 * @author Evren Sirin
 *
 */
public class OWLCacheImpl implements OWLCache {
	protected Properties cache = new Properties();
	protected String localCacheDirectory = null;
	protected boolean forced = true;
	protected boolean silent = true;
	
	/**
	 * Forces the readers to use the cached copies of the files even if the remote
	 * file may be available. When the forcing of cache is enabled the reader will
	 * first check if the cached copy exists and then only try to use the remote file
	 * when there is no cached copy.
	 * 
	 * @param b
	 */
	public void setForced(boolean b) {
		forced = b;
	}
	
	/**
	 * Returns if the using of cache is forced.
	 * 
	 * @return
	 */
	public boolean isForced() {
		return forced;
	}
	
	/**
	 * Sets the cache dir for the inference engine to find the cached files when a
	 * file cannot be downparseed from its original URL. The cache dir should include
	 * a file named service.idx. This index file is a text file where each line is in
	 * the format
	 * [service description url]=[local filename]
	 * 
	 * The ':' characters in the url's should be escaped as "\:"  
	 * 
	 * @param dir sets the local cache directory. if null it forces not to use the cache.
	 * 			  if the given dir or index file inthat dir does not exist then nothing is
	 * 			  done   
	 */
	public void setLocalCacheDirectory( String dir ) {
		localCacheDirectory = dir;
		if(dir == null) {
			cache.clear();
			System.out.println("INFO: Local cache directory is disabled");
		}
		else {
			String indexFileName = localCacheDirectory + File.separator + "service.idx"; 
			try {
				File indexFile = new File( indexFileName );
	
				cache = new Properties();				
			
				cache.load(new FileInputStream(indexFile));
	
				System.out.println("INFO: Cache has been initialized with " + cache.size() + " entries");
			} catch(FileNotFoundException e) {
				System.err.println("ERROR: Cache index file " + indexFileName + " cannot be found");
				localCacheDirectory = null;
			} catch(IOException e) {
				System.err.println("ERROR: Cache index file " + indexFileName + " has an invalid format");
				localCacheDirectory = null;
			}	
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.mindswap.owl.OWLCache#getLocalCacheDirectory()
	 */
	public String getLocalCacheDirectory() {
		return localCacheDirectory;
	}

	
	/**
	 * Returns the cached File object for the given URI. Returns null if there isn't an entry in the
	 * cache for the given file or cached file is not found.
	 * 
	 * @param fileURI
	 * @return
	 */
	public InputSource getCachedFile(String fileURI) {		
	    InputSource in = null;
		
		try {
			if(cache == null) return null;
			
			if(fileURI.endsWith("#")) fileURI = fileURI.substring(0, fileURI.length() - 1);
			
			String localFileName = cache.getProperty(fileURI);
			if(localFileName != null) {
			    File file = new File(localFileName);
			    if(!file.exists() && localCacheDirectory != null)
			        file = new File(localCacheDirectory + File.separator + localFileName);			    
				if(!file.exists()) {
					System.out.println("WARNING: Cached file does not exist " + file);
				}
				else {
					if (!silent)
						System.err.println("INFO: Using cached file " + file + " for URI " + fileURI);
				    in = new InputSource(new FileInputStream(file));
				}
			}
		}
		catch(Exception e) {
		}
		
		return in;		
	}
	
	
	/**
	 * Add a local file to be used as a cached copy for the given URI.
	 * 
	 * @param fileURI URI for the remote file
	 * @param localFile Path for the local cached copy
	 */
	public void addCachedFile(String fileURI, File localFile) {	
		cache.setProperty(fileURI, localFile.getName());
	}
	
	public void updateCachedFile(URI uri) {
	}
	

	/*
	 *  (non-Javadoc)
	 * @see org.mindswap.owl.OWLCache#updateIndexFile()
	 */
	public void updateIndexFile() {
		String indexFileName = getLocalCacheDirectory() + File.separator + "service.idx";
		try {
			cache.store(new FileOutputStream(indexFileName), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeCachedFile(URI uri, File localFile) {
		if (localFile.exists()) 
			localFile.delete();				
		
		if (cache.containsKey(uri)) {
			cache.remove(uri);
			updateIndexFile();
		}

	}
}
