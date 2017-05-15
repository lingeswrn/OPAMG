package in.opamg.app.Models;

public class Layers {

	//private variables

	String _code;
	String _description;

	public String get_code() {
		return _code;
	}

	public void set_code(String _code) {
		this._code = _code;
	}

	public String get_description() {
		return _description;
	}

	public void set_description(String _description) {
		this._description = _description;
	}

	// Empty constructor
	public Layers() {

	}

	// constructor
	public Layers(String id, String ModelNumber){
		this._code = id;
		this._description = ModelNumber;	}

}
