<?php
//include "rest.inc.php";

class webApp {	
	public $data = "";
	const DB_SERVER = "localhost";
	const DB_USER = "";
	const DB_PASSWORD = "";
	const DB = "";

	private $db = NULL;

	public function __construct()
	{
		//parent::__construct();// Init parent contructor
		$this->dbConnect();// Initiate Database connection
	}

	//Database connection
	private function dbConnect()
	{
		$this->db = mysql_connect(self::DB_SERVER,self::DB_USER,self::DB_PASSWORD);
		if($this->db) {
			$db_selector =mysql_select_db(self::DB,$this->db);
			if (!$db_selector){
				die(mysql_error());
			} 
		 } else {
		 	die(mysql_error());
		 }
	}


    public function getallvideos()
    {
    	$query = "select * from Answer where feedback is NULL";
    	$videos = $this->displaydata($query);
    	return $videos;
    }

    public function updatevideo()
    {
    	$id = $_REQUEST['id'];
		$feedback = $_REQUEST['feedback'];
    	$query = "update Answer set feedback = '$feedback' where id = $id";
    	mysql_query($query);
    }

	public function displaydata($query)
	{
		$result = [];
		$sql = mysql_query($query, $this->db);
		if (mysql_num_rows($sql) >0){
			while ($row = mysql_fetch_assoc($sql)) {
    			$result[] = $row;
    		}
    		return $result;
		}
	}
}

$action = $_REQUEST['action'];
if (!empty($action)) {
	$obj = new webApp;
	$obj->updatevideo();
}
?>
