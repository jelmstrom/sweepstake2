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
                                <td>
                                    <span class="label label-default">Username</span>
                                </td>
                                <td>
                                    <span id="userinfo-username">...</span>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <span class="label label-default">email</span>

                                </td>
                                <td>
                                    <span id="userinfo-email">...</span>

                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <span class="label label-default">admin?</span>

                                </td>
                                <td>
                                    <span id="userinfo-isadmin">...</span>

                                </td>
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
            </div>
            <div class="row">
                <input type="text" name="userinfo-league-name"
                       id="userinfo-league-name"
                       class="form-control input-lg"
                       placeholder="league name" required
                />
                <button id="userifo-create-league" class="btn btn-success btn-sm">New</button>
            </div>
        </div>
    </div>
    <script>
        this.on('mount', function() {

            var user ={
                drawLeagueTable : function (){
                    var userLeagues = [];
                    if(main.user()){
                        userLeagues = main.user().leagues;
                    }
                    console.log(userLeagues);
                    console.log(main.user());
                    var jQuery = $('#userinfo-league-table').DataTable();
                    if(jQuery) {
                        jQuery.destroy();
                    }

                    var leagues = $('#userinfo-league-table').DataTable({
                        "responsive": true,
                        "paging":false,
                        "searching": false,
                        "info" : false,
                        "ordering" : false,
                        "aaData": userLeagues,
                        "columns": [
                            { "data": "leagueName" },
                            { "data": null }
                        ],
                        "fnRowCallback": function( nRow, aData, iDisplayIndex ) {
                            return nRow;
                        },
                    });
                }
            };

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

            user.drawLeagueTable();

        });
    </script>
</user>