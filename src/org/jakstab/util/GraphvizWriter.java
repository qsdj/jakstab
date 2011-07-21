/*
 * GraphvizWriter.java - This file is part of the Jakstab project.
 * Copyright 2007-2011 Johannes Kinder <kinder@cs.tu-darmstadt.de>
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, see <http://www.gnu.org/licenses/>.
 */

package org.jakstab.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.jakstab.util.Logger;

/**
 * @author Johannes Kinder
 */
public class GraphvizWriter implements GraphWriter {

	@SuppressWarnings("unused")
	private final static Logger logger = Logger.getLogger(GraphvizWriter.class);

	private final OutputStreamWriter out;
	private String filename;

	public GraphvizWriter(String filename) throws IOException {
		this.filename = filename + ".dot";
		out = new OutputStreamWriter(new FileOutputStream(this.filename));
		out.write("digraph ");
		out.write("G");
		out.write(" {\n");
		out.write("node[shape=rectangle,style=filled,fillcolor=lightsteelblue,color=lightsteelblue]\n");
		out.write("bgcolor=\"transparent\"\n");
	}

	/*
	 * @see org.jakstab.util.GraphWriter#close()
	 */
	@Override
	public void close() throws IOException {
		out.write("}\n");
		out.close();
	}

	/*
	 * @see org.jakstab.util.GraphWriter#writeNode(java.lang.String, java.lang.String)
	 */
	@Override
	public final void writeNode(String id, String body) throws IOException {
		writeNode(id, body, null);
	}
	
	/*
	 * @see org.jakstab.util.GraphWriter#writeNode(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public final void writeNode(String id, String body, Map<String,String> properties) throws IOException { 
		out.write(toIdentifier(id));
		out.write("[label=\"");
		out.write(body.replace("\n", "\\n"));
		out.write("\"");
		if (properties != null && properties.size() > 0) {
			for (Map.Entry<String, String> property : properties.entrySet()) {
				out.write(",");
				out.write(property.getKey());
				out.write("=\"");
				out.write(property.getValue());
				out.write("\"");
			}
		}
		out.write("];\n");
	}

	/*
	 * @see org.jakstab.util.GraphWriter#writeEdge(java.lang.String, java.lang.String)
	 */
	@Override
	public final void writeEdge(String id1, String id2) throws IOException {
		writeEdge(id1, id2, null);
	}
	
	/*
	 * @see org.jakstab.util.GraphWriter#writeEdge(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public final void writeEdge(String id1, String id2, Map<String,String> properties) throws IOException { 
		out.write(toIdentifier(id1));
		out.write(" -> ");
		out.write(toIdentifier(id2));
		if (properties != null && properties.size() > 0) {
			out.write(" [");
			boolean first = true;
			for (Map.Entry<String, String> property : properties.entrySet()) {
				if (first) first = false;
				else out.write(",");
				out.write(property.getKey());
				out.write("=\"");
				out.write(property.getValue());
				out.write("\"");
			}
			out.write("]");
		}
		out.write(";\n");
	}

	/*
	 * @see org.jakstab.util.GraphWriter#writeLabeledEdge(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public final void writeLabeledEdge(String id1, String id2, String label) throws IOException {
		Map<String,String> map = new HashMap<String, String>();
		map.put("label", label.replace("\n", "\\n"));
		writeEdge(id1, id2, map);
	}
	
	private static final String toIdentifier(String id) {
		id = id.replace('@', '_');
		id = id.replace('.', '_');
		id = id.replace(':', '_');
		id = id.replace('-', '_');
		if (!Character.isLetter(id.charAt(0)))
			return "a" + id;
		else return id;
	}

	@Override
	public String getFilename() {
		return filename;
	}

}