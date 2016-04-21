<register>
<div id="div-register">
    <div class="row centered-form login">
        <div class="col-xs-12 col-sm-8 col-sm-offset-2">
            <div class="panel panel-aptifooty">
                <div class="panel-heading">
                    <h1 class="panel-title">Register here!</h1>
                </div>
                <div class="panel-body" id="div-user-register">
                    <form role="form" id="frm-new-user">
                        <div class="form-group">
                            <input type="text" name="username" id="username" class="form-control input-lg" placeholder="Username" required>
                        </div>
                        <div class="form-group">
                            <input type="email" name="email" id="email" class="form-control input-lg" placeholder="Email address" required>
                        </div>
                        <div class="form-group">
                            <input type="password" name="password" id="password" class="form-control input-lg" placeholder="password" required>
                        </div>
                        <input type="submit" value="Register" id="btn-new-user" class="btn btn-lg btn-default btn-block">
                    </form>
                    <a href="#" id="lnk-user-login">Login </a>
                </div>
                <div class="panel-body" id="div-user-login">
                    <form role="form" id="frm-login">
                        <div class="form-group">
                            <input type="text" name="username" id="login-username" class="form-control input-lg" placeholder="Username" required>
                        </div>
                        <div class="form-group">
                            <input type="password" name="password" id="login-password" class="form-control input-lg" placeholder="Password" required>
                        </div>
                        <input type="submit" value="Login" id="btn-login-user" class="btn btn-lg btn-default btn-block">
                    </form>
                    <a href="#" id="lnk-user-register">Register</a>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    this.on('mount', function() {

        $("#lnk-user-login").on("click", function() {
                console.log("login-lnk");
                $("#div-user-login").show();
                $("#div-user-register").hide();
        });
        $("#lnk-user-register").on("click", function() {
                console.log("reg-lnk");
                $("#div-user-login").hide();
                $("#div-user-register").show();
        });

        $("#btn-login-user").on("click", function(event) {
            event.preventDefault();
            var user = {
                username : $("#login-username").val(),
                password : $("#login-password").val(),
            };
            console.log(user)
            main.makeAjaxCall('/rest/user/login', 'POST', user)
                    .done(function(response){
                        main.session.set("user", response);
                        main.session.set("password", user.password);
                        $("#tag-register").hide();
                        menu.userLoggedIn();
                    }).fail(function(response){
                console.log(response);
                //update an alert tag and pop it up?
                //set an error label? <- probably neater..
            });
        });


        $("#btn-new-user").on("click", function(event) {
            event.preventDefault();
            var user = {
                username : $("#username").val(),
                email : $("#email").val(),
                password : $("#password").val(),
            };
            console.log(user)
            main.makeAjaxCall('/rest/user', 'POST', user)
                .done(function(response){
                    main.session.set("user", response);
                    main.session.set("password", user.password);
                    $("#tag-register").hide();
                    menu.userLoggedIn();
                }).fail(function(response){
                    console.log(response);
                    //update an alert tag and pop it up?
                    //set an error label? <- probably neater..
                });
        });
    });

</script>
</register>