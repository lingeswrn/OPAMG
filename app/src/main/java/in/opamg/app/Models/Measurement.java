package in.opamg.app.Models;

public class Measurement {

	int _id;
	int _project_id;
	int _equipement_id;
	String _layer_code;
	String _lattitude;
	String _longitude;
	String _utm_zone;
	String _utm_easting;
	String _utm_northing;
	String _angle_redians;
	String _cs_offset_e;
	String _cs_offset_n;
	String _el;
	String _mapping_ch;
	String _ch_by_auto_level;
	String _measurment_ch;
	String _gps_offset_length;
	String _bs_offset;
	String _is_offset;
	String _fs_offset;
	String _n_offset;
	String _e_offset;
	String _l_section_offset;
	String _x_section_offset;
	String _rise_plus;
	String _fall_minus;
	String _avg_hight_of_instrument_from_gl;
	String _hight_of_instrument;
	String _calculated_reduce_rl;
	String _checked_reduce_level;
	String _remarks;
	String _adj_rl;
	String _adjustment_error;
	String _tbm_rl;
	String _bs_angle;
	String _fs_angle;
	String _close_photograph;
	String _location_photograph;
	String _screen_shot;
	String _other_photograph;
	int _status;
	String _created_date;

	// Empty constructor
	public Measurement() {

	}

	// constructor
	public Measurement(int id, int project_id, int equipement_id, String layer_code,
					   String lattitude, String longitude, String utm_zone, String utm_easting,
					   String utm_northing, String angle_redians, String cs_offset_e, String cs_offset_n,
						String el, String mapping_ch, String ch_by_auto_level, String measurment_ch, String gps_offset_length,
					   String bs_offset, String is_offset, String fs_offset, String n_offset, String e_offset,
					   String l_section_offset, String x_section_offset, String rise_plus, String fall_minus,
					   String avg_hight_of_instrument_from_gl, String hight_of_instrument, String calculated_reduce_rl,
					   String checked_reduce_level, String remarks, String adj_rl, String adjustment_error, String tbm_rl,
					   String bs_angle, String fs_angle, String close_photograph, String location_photograph,
					   String screen_shot, String other_photograph, int status, String created_date){
		this._id = id;
		this._project_id = project_id;
		this._equipement_id = equipement_id;
		this._layer_code = layer_code;
		this._lattitude = lattitude;
		this._longitude = longitude;
		this._utm_zone = utm_zone;
		this._utm_easting = utm_easting;
		this._utm_northing = utm_northing;
		this._angle_redians = angle_redians;
		this._cs_offset_e = cs_offset_e;
		this._cs_offset_n = cs_offset_n;
		this._el = el;
		this._mapping_ch = mapping_ch;
		this._ch_by_auto_level = ch_by_auto_level;
		this._measurment_ch = measurment_ch;
		this._gps_offset_length = gps_offset_length;
		this._bs_offset = bs_offset;
		this._is_offset = is_offset;
		this._fs_offset = fs_offset;
		this._n_offset = n_offset;
		this._e_offset = e_offset;
		this._l_section_offset = l_section_offset;
		this._x_section_offset = x_section_offset;
		this._rise_plus = rise_plus;
		this._fall_minus = fall_minus;
		this._avg_hight_of_instrument_from_gl = avg_hight_of_instrument_from_gl;
		this._hight_of_instrument = hight_of_instrument;
		this._calculated_reduce_rl = calculated_reduce_rl;
		this._checked_reduce_level = checked_reduce_level;
		this._remarks = remarks;
		this._adj_rl = adj_rl;
		this._adjustment_error = adjustment_error;
		this._tbm_rl = tbm_rl;
		this._bs_angle = bs_angle;
		this._fs_angle = fs_angle;
		this._close_photograph = close_photograph;
		this._location_photograph = location_photograph;
		this._screen_shot = screen_shot;
		this._other_photograph = other_photograph;
		this._status = status;
		this._created_date = created_date;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int get_project_id() {
		return _project_id;
	}

	public void set_project_id(int _project_id) {
		this._project_id = _project_id;
	}

	public int get_equipement_id() {
		return _equipement_id;
	}

	public void set_equipement_id(int _equipement_id) {
		this._equipement_id = _equipement_id;
	}

	public String get_layer_code() {
		return _layer_code;
	}

	public void set_layer_code(String _layer_code) {
		this._layer_code = _layer_code;
	}

	public String get_lattitude() {
		return _lattitude;
	}

	public void set_lattitude(String _lattitude) {
		this._lattitude = _lattitude;
	}

	public String get_longitude() {
		return _longitude;
	}

	public void set_longitude(String _longitude) {
		this._longitude = _longitude;
	}

	public String get_utm_zone() {
		return _utm_zone;
	}

	public void set_utm_zone(String _utm_zone) {
		this._utm_zone = _utm_zone;
	}

	public String get_utm_easting() {
		return _utm_easting;
	}

	public void set_utm_easting(String _utm_easting) {
		this._utm_easting = _utm_easting;
	}

	public String get_utm_northing() {
		return _utm_northing;
	}

	public void set_utm_northing(String _utm_northing) {
		this._utm_northing = _utm_northing;
	}

	public String get_angle_redians() {
		return _angle_redians;
	}

	public void set_angle_redians(String _angle_redians) {
		this._angle_redians = _angle_redians;
	}

	public String get_cs_offset_e() {
		return _cs_offset_e;
	}

	public void set_cs_offset_e(String _cs_offset_e) {
		this._cs_offset_e = _cs_offset_e;
	}

	public String get_cs_offset_n() {
		return _cs_offset_n;
	}

	public void set_cs_offset_n(String _cs_offset_n) {
		this._cs_offset_n = _cs_offset_n;
	}

	public String get_el() {
		return _el;
	}

	public void set_el(String _el) {
		this._el = _el;
	}

	public String get_mapping_ch() {
		return _mapping_ch;
	}

	public void set_mapping_ch(String _mapping_ch) {
		this._mapping_ch = _mapping_ch;
	}

	public String get_ch_by_auto_level() {
		return _ch_by_auto_level;
	}

	public void set_ch_by_auto_level(String _ch_by_auto_level) {
		this._ch_by_auto_level = _ch_by_auto_level;
	}

	public String get_measurment_ch() {
		return _measurment_ch;
	}

	public void set_measurment_ch(String _measurment_ch) {
		this._measurment_ch = _measurment_ch;
	}

	public String get_gps_offset_length() {
		return _gps_offset_length;
	}

	public void set_gps_offset_length(String _gps_offset_length) {
		this._gps_offset_length = _gps_offset_length;
	}

	public String get_bs_offset() {
		return _bs_offset;
	}

	public void set_bs_offset(String _bs_offset) {
		this._bs_offset = _bs_offset;
	}

	public String get_is_offset() {
		return _is_offset;
	}

	public void set_is_offset(String _is_offset) {
		this._is_offset = _is_offset;
	}

	public String get_fs_offset() {
		return _fs_offset;
	}

	public void set_fs_offset(String _fs_offset) {
		this._fs_offset = _fs_offset;
	}

	public String get_n_offset() {
		return _n_offset;
	}

	public void set_n_offset(String _n_offset) {
		this._n_offset = _n_offset;
	}

	public String get_e_offset() {
		return _e_offset;
	}

	public void set_e_offset(String _e_offset) {
		this._e_offset = _e_offset;
	}

	public String get_l_section_offset() {
		return _l_section_offset;
	}

	public void set_l_section_offset(String _l_section_offset) {
		this._l_section_offset = _l_section_offset;
	}

	public String get_x_section_offset() {
		return _x_section_offset;
	}

	public void set_x_section_offset(String _x_section_offset) {
		this._x_section_offset = _x_section_offset;
	}

	public String get_rise_plus() {
		return _rise_plus;
	}

	public void set_rise_plus(String _rise_plus) {
		this._rise_plus = _rise_plus;
	}

	public String get_fall_minus() {
		return _fall_minus;
	}

	public void set_fall_minus(String _fall_minus) {
		this._fall_minus = _fall_minus;
	}

	public String get_avg_hight_of_instrument_from_gl() {
		return _avg_hight_of_instrument_from_gl;
	}

	public void set_avg_hight_of_instrument_from_gl(String _avg_hight_of_instrument_from_gl) {
		this._avg_hight_of_instrument_from_gl = _avg_hight_of_instrument_from_gl;
	}

	public String get_hight_of_instrument() {
		return _hight_of_instrument;
	}

	public void set_hight_of_instrument(String _hight_of_instrument) {
		this._hight_of_instrument = _hight_of_instrument;
	}

	public String get_calculated_reduce_rl() {
		return _calculated_reduce_rl;
	}

	public void set_calculated_reduce_rl(String _calculated_reduce_rl) {
		this._calculated_reduce_rl = _calculated_reduce_rl;
	}

	public String get_checked_reduce_level() {
		return _checked_reduce_level;
	}

	public void set_checked_reduce_level(String _checked_reduce_level) {
		this._checked_reduce_level = _checked_reduce_level;
	}

	public String get_remarks() {
		return _remarks;
	}

	public void set_remarks(String _remarks) {
		this._remarks = _remarks;
	}

	public String get_adj_rl() {
		return _adj_rl;
	}

	public void set_adj_rl(String _adj_rl) {
		this._adj_rl = _adj_rl;
	}

	public String get_adjustment_error() {
		return _adjustment_error;
	}

	public void set_adjustment_error(String _adjustment_error) {
		this._adjustment_error = _adjustment_error;
	}

	public String get_tbm_rl() {
		return _tbm_rl;
	}

	public void set_tbm_rl(String _tbm_rl) {
		this._tbm_rl = _tbm_rl;
	}

	public String get_bs_angle() {
		return _bs_angle;
	}

	public void set_bs_angle(String _bs_angle) {
		this._bs_angle = _bs_angle;
	}

	public String get_fs_angle() {
		return _fs_angle;
	}

	public void set_fs_angle(String _fs_angle) {
		this._fs_angle = _fs_angle;
	}

	public String get_close_photograph() {
		return _close_photograph;
	}

	public void set_close_photograph(String _close_photograph) {
		this._close_photograph = _close_photograph;
	}

	public String get_location_photograph() {
		return _location_photograph;
	}

	public void set_location_photograph(String _location_photograph) {
		this._location_photograph = _location_photograph;
	}

	public String get_screen_shot() {
		return _screen_shot;
	}

	public void set_screen_shot(String _screen_shot) {
		this._screen_shot = _screen_shot;
	}

	public String get_other_photograph() {
		return _other_photograph;
	}

	public void set_other_photograph(String _other_photograph) {
		this._other_photograph = _other_photograph;
	}

	public int get_status() {
		return _status;
	}

	public void set_status(int _status) {
		this._status = _status;
	}

	public String get_created_date() {
		return _created_date;
	}

	public void set_created_date(String _created_date) {
		this._created_date = _created_date;
	}
}
