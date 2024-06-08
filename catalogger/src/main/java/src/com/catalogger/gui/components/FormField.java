package com.catalogger.gui.components;

public class FormField {
	private String type      = "";
    private String label     = "";
    private String value     = "";
    private boolean editable = true;
    private String endTag    = "";
    private String options   = "";

	public FormField() {
	}

    public FormField(String t, String l, String v) {
        this.type = t;
        this.label = l;
        this.value = v;
        this.endTag = "</input>";
    }

    public String toString() {
        String result = String.format("%s name='%s' id='%s' ", this.type, this.label, this.label);
		if (this.type.contains("checkbox")) {
			if (this.value.equals("true")) {
				result += " checked='true' ";
			}
		} else {
			result += String.format(" value='%s'", this.value);
		}
        if (!this.editable) {
             result += " readonly disabled=\"true\"";
        }
        result += ">";
        if (!this.options.equals("")) {
            result += this.options;
        }
        result += this.endTag;
        return result;
    }

    public void setType(String a) { this.type = a; }
    public void setLabel(String a){ this.label = a; }
    public void setValue(String a) { this.value = a; }
    public void setEditable(boolean a) { this.editable = a; }
    public String getType() { return this.type; }
    public String getLabel() { return this.label; }
 	public String getValue() { return this.value; }
    public boolean getEditable() { return this.editable; }
    public void setEndTag(String a) { this.endTag = a; }
    public void setOptions(String a) { this.options = a; }
}

