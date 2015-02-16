<?php
include "rest.inc.php";

class API extends REST {	
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
			} else {
		 		$this->processApi();
		 	}
		 } else {
		 	die(mysql_error());
		 }
	}

	public function processApi()
	{
	
		$func = strtolower(trim(str_replace("/","",$_REQUEST['rquest'])));
		if((int)method_exists($this,$func) > 0)
			$this->$func();
		else
			$this->response('',404);				// If the method not exist with in this class, response would be "Page not found".
    }

    public function getalltopics()
    {
    	$query = "select * from Topic";
    	$this->displaydata($query);
    }

    public function json($data){
    	header ('Content-Type: application/json');
		return json_encode($data);
			
    }

    

    public function getallquestions()
	{
		$topicId = $_REQUEST['TopicID'];
		$query = "select * from Questions where topicid =" . $topicId;
		$this->displaydata($query);
	}
	public function getallanswers()
	{
		$assessmentId = $_REQUEST['assessmentID'];
		$query = "select * from Answer by assessmentid =" . $assessmentId;
		$this->displaydata($query);
	}
	public function getallassessments()
	{
		$userId = $_REQUEST['UserID'];
		$query = "select * from Assessment by userid = " . $userId;
		$this->displaydata($query);
	}
	public function getuserdata()
	{
		$userId = $_REQUEST['UserID'];
		$query = "select * from Answer where assessmentid in (select assessmentid from Assessment where userid=" .$userId. ")";
		$result = [];
		$sql = mysql_query($query, $this->db);
		if (mysql_num_rows($sql) >0){
			while ($row = mysql_fetch_assoc($sql)) {
    			$result[] = $row;
    		}
    		$questionIds = [];
    		foreach($result as $key => $val) {
    			$questionIds[] = $val['questionid'];
    		}

    		$allIds = implode(',', $questionIds);
    		$query = "select question from Questions where id in (" . $allIds.")";
    		$sql = mysql_query($query, $this->db);$i=0;
    		while ($row = mysql_fetch_assoc($sql)) {
    			$result[$i]['question'] = $row['question'];
    			$i++;
    		}
    		//print_R($result);
    		$data = $this->json($result);
    		echo $data;
    		exit;
		}

		//$this->displaydata($query);	
	}

	public function createassessment()
	{
		$userId = $_REQUEST['UserID'];	
		$sql = "INSERT INTO Assessment (userid)
		VALUES ($userId)";
		if (mysql_query($sql)) {
			$result['id'] = mysql_insert_id();
			echo $this->json($result);
			exit;
		}
	}

	public function insertvideo()
	{
		$userId = $_REQUEST['UserID'];
		$assessmentId = $_REQUEST['AssessmentID'];
		$questionId = $_REQUEST['QuestionID'];
		$videoLink = $_REQUEST['VideoLink'];
		$sql = "INSERT INTO Answer (userid,assessmentid,videolink,questionid) 
		VALUES ($userId, $assessmentId, '$videoLink', $questionId)";
		if (mysql_query($sql)){
			$result['id'] = mysql_insert_id();
			$result['userid'] = $userId;
			$result['assessmentid'] = $assessmentId;
			$result['questiondid'] = $questionId;
			$result['videolink'] = $videoLink;
			echo $this->json($result);
			exit;
		}
	}

	public function createquestion()
	{
		$userId = $_REQUEST['UserID'];
		$assessmentId = $_REQUEST['AssessmentID'];
		$questionId = $_REQUEST['QuestionID'];
		$result = [];

		if (!empty($_FILES['file']['name'])) {
			$filepath = "/video/" . $_FILES['file']['name'];
			chmod($filepath , 0777);
			if (move_uploaded_file($_FILES["file"]["tmp_name"] , $filepath)) {
				$videolink = $filepath;
				$sql = "INSERT INTO Answer(userid,assessmentid,videolink,questionid)
				VALUES ($userId, $AssessmentId, $videolink, $questionId)";
				if (mysql_query($sql)) {
					$result['id'] = mysql_insert_id();
					echo $this->json($result);
					exit;
				}
			} else {
				$result['error'] = $_FILES['file']['error'];
				echo $this->json($result);
				exit;
			}
		} else {
			$result['error'] = "File is empty";
			echo $this->json($result);
			exit;
		}
	}

	public function displaydata($query)
	{
		$result = [];
		$sql = mysql_query($query, $this->db);
		if (mysql_num_rows($sql) >0){
			while ($row = mysql_fetch_assoc($sql)) {
    			$result[] = $row;
    		}
    		$data = $this->json($result);
    		echo $data;
    		exit;
		}
	}


}

$api = new API;


?>
