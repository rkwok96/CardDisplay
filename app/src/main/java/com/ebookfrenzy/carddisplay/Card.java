package com.ebookfrenzy.carddisplay;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable{
	String rawInfo;
	String name;
	String text;
	String card_type;
	String type;
	String family;
	int atk;
	int def;
	int level;


	public Card(String rawInfo, String name, String text, String card_type, String type, String family, int atk, int def, int level){
		this.rawInfo = rawInfo;
		this.name = name;
		this.text = text;
		this.card_type = card_type;
		this.family = family;
		this.atk = atk;
		this.def = def;
		this.level = level;
	}
	
	public Card() {}

	protected Card(Parcel in){
	    name = in.readString();
	    text = in.readString();
	    card_type = in.readString();
	    type = in.readString();
	    family = in.readString();
	    atk = in.readInt();
	    def = in.readInt();
	    level = in.readInt();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>(){
	    @Override
        public Card createFromParcel(Parcel in){
	        return new Card(in);
        }

        @Override
        public Card[] newArray(int size){
            return new Card[size];
        }
    };

	public String getRawInfo() { return rawInfo; }

	public void setRawInfo(String rawInfo) { this.rawInfo = rawInfo; }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public String print() {
		String cardString = "Name: " + name +"\n"
				+ "Text: " + text + "\n"
				+ "Card Type: " + card_type + "\n"
				+ "Type: " + type + "\n"
				+ "Family: " + family + "\n"
				+ "Atk: " + atk + "\n"
				+ "Def: " + def + "\n"
				+ "Level: " + level + "\n";
//		System.out.println(cardString);
		return cardString;
	}

	@Override
    public int describeContents(){
	    return 0;
    }

	@Override
    public void writeToParcel(Parcel dest, int flags){
	    dest.writeString(name);
	    dest.writeString(text);
	    dest.writeString(card_type);
	    dest.writeString(type);
	    dest.writeString(family);
	    dest.writeInt(atk);
	    dest.writeInt(def);
	    dest.writeInt(level);
    }

	

}
