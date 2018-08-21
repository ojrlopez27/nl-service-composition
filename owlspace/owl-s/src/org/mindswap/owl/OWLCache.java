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
package org.mindswap.owl;

import java.io.File;
import java.net.URI;

import org.xml.sax.InputSource;

/**
 * 
 * 
 * 
 * @author Evren Sirin
 *
 */
public interface OWLCache {
	/**
	 * Forces the readers to use the cached copies of the files even if the remote
	 * file may be available. When the forcing of cache is enabled the reader will
	 * first check if the cached copy exists and then only try to use the remote file
	 * when there is no cached copy.
	 * 
	 * @param b
	 */
	public void setForced(boolean b);
	
	/**
	 * Returns if the using of cache is forced.
	 * 
	 * @return
	 */
	public boolean isForced();
	
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
	public void setLocalCacheDirectory(String dir);

	/**
	 * Returns the full path to the local cache directory as a string
	 * @return a String indicating the path to the local cache directory
	 */
	public String getLocalCacheDirectory();
	
	
	/**
	 * Returns the cached source for the given URI. Returns null if there isn't an entry in the
	 * cache for the given file or cached file is not found.
	 * 
	 * @param fileURI
	 * @return
	 */
	public InputSource getCachedFile(String fileURI);
	
	
	/**
	 * Add a local file to be used as a cached copy for the given URI.
	 * 
	 * @param fileURI URI for the remote file
	 * @param localFile File for the local cached copy
	 */
	public void addCachedFile(String fileURI, File localFile);
	
	/**
	 * Attempt to update a cached file from remote site. The key can 
	 * be retrieved from uri. 
	 * 
	 * <p><b>WARNING</b>: Default implementation will not do anything when this function is called but this 
	 * behaviour can be overridden with a new OWLCache implementation that saves the remote file 
	 * locally and calls the addCachedFile() function so subsequent read operations will use the 
	 * local file.
	 * 
	 * @param uri update the local cache by using the latest content from uri.
	 */
	public void updateCachedFile(URI uri);
	
	/**
	 * Saves the current status of the index file (service.idx) persistently. 
	 * All changes performed during the running session by calls of 
	 * <code>addCachedFile(String, File)</code> are stored.
	 * 
	 * @see addCachedFile(String fileURI, File localFile)
	 */
	public void updateIndexFile();
	
	public void removeCachedFile(URI uri, File localFile);
}
