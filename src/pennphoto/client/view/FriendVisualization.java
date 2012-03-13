package pennphoto.client.view;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;



/**
 * This class is a thin layer over the Javascript Visualization Toolkit, http://thejit.org
 * 
 * @author zives
 *
 */
public class FriendVisualization {
	
	public static final native void addToGraph(final JavaScriptObject ht, final String json) /*-{
		var content = JSON.parse(json);
		ht.op.sum(content, { type: "fade:con", fps: 4, duration: 1000, hideLabels: true }); 
		ht.refresh();
	}-*/;
	
	public static final native void clear(final JavaScriptObject ht, final String id) /*-{
		var node = ht.graph.getNode(id);
		var subnodes = node.getSubnodes(1);
		for (var i = 0; i < subnodes.length; i++) {	
			subnodes[i].eachAdjacency(function(adj) {
				ht.graph.removeAdjacence(adj.nodeFrom.id, adj.nodeTo.id);
			});	
			ht.graph.removeNode(subnodes[i].id);
		}
		ht.labels.clearLabels();
		ht.refresh();
	}-*/;
	
	/**
	 * Creates a JavaScript Infovis Toolkit hypertree
	 * 
	 * @param json
	 * @return
	 */
	public static final native JavaScriptObject createGraph(final String json, final FriendBrowser parent) /*-{
		var content = JSON.parse(json);

		 //init Hypertree
		var ht = new $wnd.$jit.Hypertree({
		    //id of the visualization container
		    injectInto: 'friendbrowser',
		    //By setting overridable=true,
		    //Node and Edge global properties can be
		    //overriden for each node/edge.
		    Node: {
		        overridable: true,
		        'transform': false,
		        color: "#f00"
		    },
		
		    Edge: {
		        //overridable: true,
		        color: "#088"
		    },
		    //calculate nodes offset
		    offset: 0.2,
		    //Change the animation transition type
		    transition: $wnd.$jit.Trans.Back.easeOut,
		    //animation duration (in milliseconds)
		    duration:1000,
		
		    //Attach event handlers on label creation.
		    onCreateLabel: function(domElement, node){
		        domElement.innerHTML = node.name;
		        domElement.style.cursor = "pointer";
		        domElement.onclick = function () {
					parent.@pennphoto.client.view.FriendBrowser::selectUser(ILjava/lang/String;)(node.id, node.name);
					console.debug("Clicked");
		           // ht.onClick(node.id, { hideLabels: false });
		
		        };
		    },
		    //This method is called when moving/placing a label.
		    //You can add some positioning offsets to the labels here.
		    onPlaceLabel: function(domElement, node){
		        var width = domElement.offsetWidth;
		        var intX = parseInt(domElement.style.left);
		        intX -= width / 2;
		        domElement.style.left = intX + 'px';
		    },
		
		    onAfterCompute: function(){
		    }
		});
		//load JSON graph.
		ht.loadJSON(content);
		//compute positions and plot
		ht.refresh();
		//end
		ht.controller.onBeforeCompute(ht.graph.getNode(ht.root));
		ht.controller.onAfterCompute();
		return ht;
	}-*/;
	

	/**
	 * XML to JSON converter, from http://davidwalsh.name/convert-xml-json
	 * @param xml
	 * @return
	 */
	public static native JavaScriptObject xmlToJson(final JavaScriptObject xml) /*-{
	  // Create the return object
	  var obj = {};
	
	  if (xml.nodeType == 1) { // element
	    // do attributes
	    if (xml.attributes.length > 0) {
	    obj["@attributes"] = {};
	      for (var j = 0; j < xml.attributes.length; j++) {
	        var attribute = xml.attributes.item(j);
	        obj["@attributes"][attribute.nodeName] = attribute.nodeValue;
	      }
	    }
	  } else if (xml.nodeType == 3) { // text
	    obj = xml.nodeValue;
	  }
	
	  // do children
	  if (xml.hasChildNodes()) {
	    for(var i = 0; i < xml.childNodes.length; i++) {
	      var item = xml.childNodes.item(i);
	      var nodeName = item.nodeName;
	      if (typeof(obj[nodeName]) == "undefined") {
	        obj[nodeName] = @pennphoto.client.view.FriendVisualization::xmlToJson(Lcom/google/gwt/core/client/JavaScriptObject;)(item);
	      } else {
	        if (typeof(obj[nodeName].length) == "undefined") {
	          var old = obj[nodeName];
	          obj[nodeName] = [];
	          obj[nodeName].push(old);
	        }
	        obj[nodeName].push(@pennphoto.client.view.FriendVisualization::xmlToJson(Lcom/google/gwt/core/client/JavaScriptObject;)(item));
	      }
	    }
	  }
	  return obj;
	}-*/;

	public static native JavaScriptObject parseXML(final String xml) /*-{
		if (window.DOMParser) {
			var parser = new DOMParser();
			return parser.parseFromString(xml,"text/xml");
		} else {
			var doc = new ActiveXObject("Microsoft.XMLDOM");
			doc.async="false";
			doc.loadXML(xml);
			return doc;
		}
	}-*/;

}
