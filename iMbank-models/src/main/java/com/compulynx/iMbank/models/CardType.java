package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardType {
	public int id;
	public String name;
	public String code;
	public String description;
	public int xCoordinates;
	public int yCoordinates;
	public String fontname;
	public int fontsize;
	public int fontcolor;
	public int counter;
	

	public CardType(int id, String name, String code, String description,
			int xCoordinates, int yCoordinates, String fontname, int fontsize,
			int fontcolor) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.description = description;
		this.xCoordinates = xCoordinates;
		this.yCoordinates = yCoordinates;
		this.fontname = fontname;
		this.fontsize = fontsize;
		this.fontcolor = fontcolor;
	}









	public CardType() {
		super();
		// TODO Auto-generated constructor stub
	}

}
