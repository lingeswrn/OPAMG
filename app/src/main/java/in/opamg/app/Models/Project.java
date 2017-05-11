package in.opamg.app.Models;

public class Project {

	//private variables
	String _id;
	String _from;
	String _to;
	String _project_name;
	String _company_name;
	String _work_order_number;
	String _scope_wrk;
	String _remarks;
	String _user_id;
	String _status;
	String _created_date;
	String _updated_date;

	// Empty constructor
	public Project() {

	}
	
	// constructor
	public Project(String id, String from, String to, String projectName, String companyName, String workOrder, String scopeWork, String remarks, String userId, String status, String createdDate, String updatedDate){
		this._id = id;
		this._from = from;
		this._to = to;
		this._project_name = projectName;
		this._company_name = companyName;
		this._work_order_number = workOrder;
		this._scope_wrk = scopeWork;
		this._remarks = remarks;
		this._user_id = userId;
		this._status = status;
		this._created_date = createdDate;
		this._updated_date = updatedDate;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_from() {
		return _from;
	}

	public void set_from(String _from) {
		this._from = _from;
	}

	public String get_to() {
		return _to;
	}

	public void set_to(String _to) {
		this._to = _to;
	}

	public String get_project_name() {
		return _project_name;
	}

	public void set_project_name(String _project_name) {
		this._project_name = _project_name;
	}

	public String get_company_name() {
		return _company_name;
	}

	public void set_company_name(String _company_name) {
		this._company_name = _company_name;
	}

	public String get_work_order_number() {
		return _work_order_number;
	}

	public void set_work_order_number(String _work_order_number) {
		this._work_order_number = _work_order_number;
	}

	public String get_scope_wrk() {
		return _scope_wrk;
	}

	public void set_scope_wrk(String _scope_wrk) {
		this._scope_wrk = _scope_wrk;
	}

	public String get_remarks() {
		return _remarks;
	}

	public void set_remarks(String _remarks) {
		this._remarks = _remarks;
	}

	public String get_user_id() {
		return _user_id;
	}

	public void set_user_id(String _user_id) {
		this._user_id = _user_id;
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

	public String get_updated_date() {
		return _updated_date;
	}

	public void set_updated_date(String _updated_date) {
		this._updated_date = _updated_date;
	}
}
