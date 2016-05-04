<user>
    <div id="div-user" class="row">
        <div class="col-md-10 col-md-offset-1">
            <div class="col-xs-12 col-md-6">
                <div id="row-user-details" class="row">

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">User details</h3>
                        </div>
                        <div class="panel-body">
                            <table class="table table-borderless">
                                <tr>
                                    <td><span class="label label-default">Username</span></td>
                                    <td><span id="userinfo-username">...</span></td>
                                </tr>
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
                <div id="league-list" class="row">
                    <div class="panel panel-default">

                        <div class="panel-heading">
                            <h3 class="panel-title">Leagues</h3>
                        </div>
                        <div class="panel-body">
                            <table class="table table-borderless" id="userinfo-league-table">
                                <thead>
                                <tr>
                                    <th></th> <!-- id -->
                                    <th></th> <!-- league name -->
                                    <th></th> <!-- controls -->
                                </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
                <div id="create-join-league" class="row">
                    <div class="col-xs-6 col-md-6">
                        <div class="panel panel-default">
                            <div class="input input-group">
                                <input type="text" name="userinfo-league-name"
                                       id="userinfo-league-name"
                                       class="form-control input-sm"
                                       placeholder="Name" required
                                />
                                    <span class="input-group-btn">
                                        <button id="userifo-create-league"
                                                class="btn btn-success btn-sm">Create new</button>
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
                                        <button id="userinfo-join-league"
                                                class="btn btn-info btn-sm">Join league</button>
                                    </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="userdetails-leaderboard" class="col-xs-12 col-md-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Leaderboard <span id="leaderboard-league-name"></span></h3>
                    </div>
                    <div class="panel-body">
                        <table id="table-leaderboard" class="table table-borderless">
                            <thead>
                            <tr>
                                <th>User</th>
                                <th>Points</th>
                            </tr>
                            </thead>
                            <tr>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script>
        this.on('mount', function () {
            $("#userdetails-leaderboard").hide();
            $("#userifo-create-league").on("click", function () {
                main.makeAjaxCall("/rest/league/" + $("#userinfo-league-name").val()
                        , "POST"
                        , main.user())
                        .done(function (response) {
                            main.session.set("user", response);
                            user.drawLeagueTable();
                        }).fail(function (response) {
                    console.log(response);
                    $("#alert-fail  ").html("message...")
                    $("#alert-fail").fadeTo(2000, 500).slideUp(500, function () {
                        $("#alert-fail").alert('close');
                    });
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
                    $("#alert-fail").fadeTo(2000, 500).slideUp(500, function () {
                        $("#alert-fail").alert('close');
                    });
                });
            });
            user.drawLeagueTable();
            $('#userinfo-league-table tbody').on('click', 'tr', function () {
                var data = $('#userinfo-league-table').DataTable().row( this ).data();
                console.log(data);
                $("#userdetails-leaderboard").show();
                $("#leaderboard-league-name").html(data.leagueName);
                drawLeaderboard(data.id);
            } );

            var leaderboardTable;
            drawLeaderboard = function(id){
                if(!leaderboardTable){

                    leaderboardTable = $('#table-leaderboard').DataTable({
                        "responsive": true,
                        "paging": false,
                        "searching": false,
                        "info": false,
                        "ordering": false,
                        "ajax": {
                            'url':main.buildUrl("/rest/league/"+id+"/leaderboard"),
                            'dataSrc': '',
                            'type' : 'GET',
                            'beforeSend': function (xhr) {
                                if(main.session.get("password")) {
                                    console.log("basic header :: datatable ajax");
                                    xhr.setRequestHeader("Authorization", "Basic " + btoa(main.user().username + ":" + main.session.get("password")));
                                }
                            },
                        },
                        "columns": [
                            {"data" : "username"},
                            {"data": null}
                        ],
                        "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                            console.log("aData");
                            console.log(aData);
                            return nRow;
                        },
                    });
                } else {
                    leaderboardTable.ajax.url(main.buildUrl("/rest/league/"+id+"/leaderboard"));
                    leaderboardTable.ajax.reload();
                }

            };

        });
    </script>
</user>