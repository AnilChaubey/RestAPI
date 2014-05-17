<?php
/*
 * Created on May 17, 2014
 *
 * Main index file which take care of requests received from android and process it
 * 
 */
  
if (isset($_POST['tag']) && $_POST['tag'] != '') {
 
  $tag = $_POST['tag'];

 
 if ($tag == 'auth') {


 	$username = $_POST['username'];
 	$password = $_POST['password'];
 	
 	if(strcmp($username,'user123') == 0 && strcasecmp($password,'test123') == 0)
 	{
 		$response["success"] = true;
 		$response["username"] = $username;
 		$response["message"] = "User Found.";
 		echo json_encode($response);
 		
 	}
 	else
 	{
 		$response["success"] = false;
 		$response["username"] = $username;
 		$response["message"] = "User Not Found.";
 		echo json_encode($response);
 		
 	}

    
 } 
}


?>
