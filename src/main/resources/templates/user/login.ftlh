<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>盘古| 登录</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="../../plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="../../plugins/icheck-bootstrap/icheck-bootstrap.min.css">
  <link rel="stylesheet" href="../../dist/css/adminlte.min.css">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css" rel="stylesheet" />
  <script src="../../plugins/jquery/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
  <script src="../../plugins/jquery-validation/jquery.validate.min.js"></script>
  <script src="../../plugins/jquery-validation/additional-methods.min.js"></script>
  <script src="../../pangu/md5.js"type="text/javascript"></script>
</head>


<body class="hold-transition login-page">

<div class="login-box">
  <div class="login-logo">
    <a href="https://pangu.javamall.com.cn/" target="_blank"><b>盘古</b></a>
  </div>

  <div class="card">

        <div class="card card-primary">
          <form role="form" id="quickForm">
            <div class="card-body">
              <div class="form-group">
                <label for="exampleInputEmail1">用户名</label>
                <input  name="username" class="form-control" id="exampleInputEmail1" placeholder="请填写用户名">
              </div>
              <div class="form-group">
                <label for="exampleInputPassword1">密码</label>
                <input type="password" name="pwd" class="form-control" id="password" placeholder="请填写密码">
                <input type="hidden" name="password" class="form-control" id="md5password" >
              </div>
            </div>
            <!-- /.card-body -->
            <div class="card-footer center-align">
              <button type="submit" class="center-align btn btn-primary btn-block">登录</button>
            </div>
          </form>
        </div>

  </div>
</div>


<script>
  function login () {
    var pwd= $("#password").val()
    $("#md5password").val(hex_md5(pwd))

    var data = $("#quickForm").serialize();
    $.ajax({
      url: '${context_path}/data/user/login',
      type: 'POST',
      data: data,
      dataType: 'json',
      success: function (res) {
        if (res.success) {
          location.href = '${context_path}/view/javashop/';
        }else {

          toastr.error(res.message);

        }

      },
    });
  };

  $(document).ready(function () {
    $.validator.setDefaults({
      submitHandler: function () {

        login ();

      }
    });
    $('#quickForm').validate({
      rules: {
        username: {
          required: true,
          // email: true,
        },
        pwd: {
          required: true,
          // minlength: 5
        },
        terms: {
          required: true
        },
      },
      messages: {
        username: {
          required: "请填写用户名",
          // email: "Please enter a vaild email address"
        },
        pwd: {
          required: "请填写密码",
          // minlength: "Your password must be at least 5 characters long"
        },
        // terms: "Please accept our terms"
      },
      errorElement: 'span',
      errorPlacement: function (error, element) {
        error.addClass('invalid-feedback');
        element.closest('.form-group').append(error);
      },
      highlight: function (element, errorClass, validClass) {
        $(element).addClass('is-invalid');
      },
      unhighlight: function (element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
      }
    });
  });


</script>

</body>
</html>
