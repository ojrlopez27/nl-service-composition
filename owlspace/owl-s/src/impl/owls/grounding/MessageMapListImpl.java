// The MIT License
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

package impl.owls.grounding;


import impl.owl.CastingList;

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.grounding.MessageMapList;
import org.mindswap.owls.process.Parameter;
import org.mindswap.utils.URIUtils;

public class MessageMapListImpl extends CastingList implements MessageMapList {
	public MessageMapListImpl() {
	    super(MessageMap.class);
	}

    public MessageMapListImpl(OWLIndividualList list) {
        super(list, MessageMap.class);
    }

    public MessageMap messageMapAt(int index) {
		return (MessageMap) get(index);
	}
	
	public MessageMap getMessageMap(Parameter p) {
		for (int i = 0; i < size(); i++) {
		    MessageMap messageMap = messageMapAt(i);
			if (messageMap.getOWLSParameter().equals(p))
				return messageMap;
		}
		return null;
	}
	
	public MessageMap getMessageMap(String uri) {
	    String name = URIUtils.getLocalName(uri);
	    if(name == null)
	        name = uri;
		for (int i = 0; i < size(); i++) {
		    MessageMap map = messageMapAt(i); 
			String u = map.getGroundingParameter();
			String n = URIUtils.getLocalName(u);
		    if(n == null)
		        n = uri;
			if (uri.equals(u) || name.equals(n))
				return map;
		}
		
		return null;
	}
	
	
}
