<user>
    <div id="div-user" class="row">
        <div class="col-md-10 col-md-offset-1">
            <div class="col-xs-12 col-md-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">User details</h3>
                    </div>
                    <div class="panel-body">
                        <table class="table table-borderless">
                            <tr>
                                <td><span class="label label-default">Username</span></td>
                                <td><span id="userinfo-username">...</span></td> </tr>
                            <tr>
                                <td><span class="label label-default">email</span></td>
                                <td><span id="userinfo-email">...</span></td>
                            </tr>
                            <tr>
                                <td><span class="label label-default">admin?</span></td>
                                <td><span id="userinfo-isadmin">...</span></td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-xs-12 col-md-6">
                <div class="panel panel-default">

                    <div class="panel-heading">
                        <h3 class="panel-title">Leagues</h3>
                    </div>
                    <div class="panel-body">
                        <table class="table table-borderless" id="userinfo-league-table">
                            <thead>
                            <tr>
                                <th></th> <!-- league -->
                                <th></th> <!-- controls -->
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-6 col-md-6">
                        <div class="panel panel-default">
                            <div class="input input-group">
                                <input type="text" name="userinfo-league-name"
                                       id="userinfo-league-name"
                                       class="form-control input-sm"
                                       placeholder="Name" required
                                />
                                <span class="input-group-btn">
                                    <button id="userifo-create-league" class="btn btn-success btn-sm">Create new</button>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6 col-md-6">
                        <div class="panel panel-default">
                            <div class="input input-group">
                                <input type="text" name="userinfo-join-name"
                                       id="userinfo-join-name"
                                       class="form-control input-sm"
                                       placeholder="Name" required
                                />
                                <span class="input-group-btn">
                                    <button id="userinfo-join-league" class="btn btn-info btn-sm">Join league</button>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
    <script>
        this.on('mount', function () {
            $("#userifo-create-league").on("click", function() {
                main.makeAjaxCall("/rest/league/"+$("#userinfo-league-name").val()
                            , "POST"
                            , main.user())
                    .done(function(response){
                        main.session.set("user", response);
                        user.drawLeagueTable();
                    }).fail(function(response){
                        console.log(response);
                    });
            });

            $("#userinfo-join-league").on("click", function () {
                main.makeAjaxCall("/rest/league/" + $("#userinfo-join-name").val() + "/join"
                        , "POST"
                        , main.user())
                        .done(function (response) {
                            main.session.set("user", response);
                            user.drawLeagueTable();
                            $("#userinfo-join-name").val("");
                        }).fail(function (response) {
                            console.log(response);
                            $("#alert-fail  ").html("message...")
                            $("#alert-fail").fadeTo(2000, 500).slideUp(500, function(){
                                $("#success-alert").alert('close');
                            });
                });
            });
            user.drawLeagueTable();

        });
    </script>
</user>