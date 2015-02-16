<?php
      #error_reporting(E_ALL);
      #ini_set('display_errors', 1);
      include "webapp.php";
      $obj = new webApp;
      $assessmentVideos = $obj->getallvideos();

      
    ?>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="favicon.ico">

    <title>EnterVu Feedback Tool</title>

    <!-- Bootstrap core CSS -->
    <link href="_/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style>
      footer{
        text-align:center;
      }
      .vidList{
        border:1px solid #dddddd;
        border-radius:5px;
      }
      .vidList li {
      border-bottom: 1px solid #dddddd;
      padding:10px;
      }
      .vidList li:nth-child(even){
        background-color:#dddddd;
      }
      .vidList li h3{
        margin-top:0px;
        padding:0px;
      }
      .modal-body textarea{
        width:100%;

      }
    </style>
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">EnterVu Feedback Tool</a>
        </div>
        
      </div>
    </nav>
    <script>
      function save(idvalue, model) {
        //var feedback = 'feedback' + i;
        var feedback = $('#' + model) .find('#feedback').val()
        $.ajax({
           type: 'GET',
           url : 'webapp.php',
           data : 'action=update&id=' + idvalue + '&feedback=' + feedback,
           success : function() {
            alert('saved');
            $('#' + model).hide();
            window.location.reload();
           }
        }); 
      }
    </script>
    <?php
//print_r($assessmentVideos);
    ?>
    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
      <div class="container">
        <h1>Assess Videos</h1>
        <ul class="list-unstyled vidList">
          <?php
            $i = 0;
            foreach ($assessmentVideos as $key => $video) {$i++;  
              $videolink =  $video['videolink'];
              ?>
              <li><h3>Video #<?php echo $i?></h3>
                <a data-toggle="modal" data-target="#myModal<?php echo $i;?>" class="btn btn-primary btn-lg" role="button" href="<?php echo $video['videolink']?>">Start Assesment</a>
                <a class="btn btn-danger btn-lg" href="#" role="button">Delete Assesment</a>
              </li>
              <!-- Modal -->
              <?php
                $modelId = 'myModal' . $i;
              ?>
              <div class="modal fade" id="<?php echo $modelId;?>" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                  <div class="modal-content">
                    <div class="modal-header">
                      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                      <h4 class="modal-title" id="myModalLabel">VIDEO!!</h4>

                    </div>
                    <div class="modal-body">
<iframe width="560" height="315" src="<?php echo $videolink;?>" frameborder="0" allowfullscreen></iframe>                      <hr>
                      <h4>Enter Your Feedback</h4>
                      <textarea  id="feedback"></textarea>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                      <button type="button" class="btn btn-primary" onclick="save('<?php echo $video['id']?>', '<?php echo $modelId;?>')">Submit Feedback</button>
                    </div>
                  </div>
                </div>
              </div>

          <?php }?>
        </ul>
      </div>
    </div>
    
      <hr>

      <footer>
        <p>&copy; EnterVu 2015</p>
      </footer>
    </div> <!-- /container -->




    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="_/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
  </body>
</html>