package in.opamg.app.Models;

public class Equipments {

	//private variables
	String _id;
	String _model_number;
	String _last_calibration_service_center;
	String _expiry_date;
	String _owner;

	public String get_owner() {
		return _owner;
	}

	public void set_owner(String _owner) {
		this._owner = _owner;
	}

	String _least_count;
	String _status;
	String _created_date;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_model_number() {
		return _model_number;
	}

	public void set_model_number(String _model_number) {
		this._model_number = _model_number;
	}

	public String get_last_calibration_service_center() {
		return _last_calibration_service_center;
	}

	public void set_last_calibration_service_center(String _last_calibration_service_center) {
		this._last_calibration_service_center = _last_calibration_service_center;
	}

	public String get_expiry_date() {
		return _expiry_date;
	}

	public void set_expiry_date(String _expiry_date) {
		this._expiry_date = _expiry_date;
	}

	public String get_least_count() {
		return _least_count;
	}

	public void set_least_count(String _least_count) {
		this._least_count = _least_count;
	}

	public String get_status() {
		return _status;
	}

	public void set_status(String _status) {
		this._status = _status;
	}

	public String get_created_date() {
		return _created_date;
	}

	public void set_created_date(String _created_date) {
		this._created_date = _created_date;
	}

	// Empty constructor
	public Equipments() {

	}

	// constructor
	public Equipments(String id, String ModelNumber, String Calib, String exDate, String leastCount, String owner, String status, String createdDate){
		this._id = id;
		this._model_number = ModelNumber;
		this._last_calibration_service_center = Calib;
		this._expiry_date = exDate;
		this._least_count = leastCount;
		this._owner = owner;
		this._status = status;
		this._created_date = createdDate;
	}

}
