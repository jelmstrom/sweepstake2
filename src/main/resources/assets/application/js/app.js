var main = {
    dev : true,
    ns : null,
    session : null,
    hideAllTags : function (){
        $("#tag-register").hide();
        $("#tag-user").hide();
        $("#userdetails-leaderboard").hide();
        $("#tag-group").hide();
    },
    user : function(){
        return main.session.get("user");
    },
    group : function(){
        return main.session.get("group");
    },
    hideAlerts : function(){
        $("#alert-info").hide();
        $("#alert-warning").hide();
        $("#alert-fail").hide();

    },

    buildUrl : function(endpoint) {
        if(main.dev){
            return  'http://localhost:8081'+endpoint
        } else {
            return endpoint;
        }
    },

    makeAjaxCall : function (url, type, data) {
            var deferredObject = $.Deferred();
            var msgBody;
            if(data){
                msgBody=JSON.stringify(data);
                console.log(msgBody);
            }
            url = main.buildUrl(url);
            var userName;
            if(main.session.get("user")){
                userName=main.session.get("user").username;
            }
            $.ajax({
            headers: {
                    'Content-Type': 'application/json'
                },
                type: type,
                cache: "false",
                username: userName,
                password: main.session.get("password"),
                url: url,
                data: msgBody,
                async: true,
                beforeSend: function (xhr) {
                    if(main.session.get("password")) {
                        console.log("basic header");
                        xhr.setRequestHeader("Authorization", "Basic " + btoa(userName + ":" + main.session.get("password")));
                    }
                },
                success: function (response) {
                    deferredObject.resolve(response);
                },
                error: function (jqXHR) {
                    deferredObject.reject(jqXHR);

                }
            });
            return deferredObject.promise();
        }
};

var menu = {
    userLoggedOut : function() {
        $("#menu-user").hide();
        $("#menu-register").show();
        main.hideAllTags();
    },

    userLoggedIn : function() {
        $("#menu-user").show();
        $("#p-user").text(main.session.get("user").username);
        $("#menu-register").hide();
    },
    init : function(){
        console.log("menu init");
        if(main.user()){
            menu.userLoggedIn();
        } else {
            menu.userLoggedOut();
        }
        $("#menu-register").on("click", function() {
                console.log("clicked");
                main.hideAllTags();
                $("#div-register").show();
                $("#tag-register").show();
                $("#div-user-login").hide();
                $("#div-user-register").show();
            });

        $("#menu-logout").on("click", function() {

            main.session.set("password", null);
            main.session.set("user", null);
            menu.userLoggedOut();
            var leagues = $('#userinfo-league-table').DataTable();
            leagues.data().clear();
        });
        $("#menu-details").on("click", function() {
            main.hideAllTags();

            $("#userinfo-email").text(main.user().email);
            $("#userinfo-isadmin").text(main.user().admin);
            $("#userinfo-username").text(main.user().username);
            user.drawLeagueTable();
            $("#tag-user").show();
        });
    }
};
var user = {
    drawLeagueTable: function () {
        var userLeagues = [];
        if (main.user()) {
            userLeagues = main.user().leagues;
        }
        console.log("draw table");
        console.log(userLeagues);
        console.log(main.user());
        var jQuery = $('#userinfo-league-table').DataTable();
        if (jQuery) {
            jQuery.destroy();
        }

        var leagues = $('#userinfo-league-table').DataTable({
            "responsive": true,
            "paging": false,
            "searching": false,
            "info": false,
            "ordering": false,
            "aaData": userLeagues,
            "columns": [
                {"data" : "id", visible : false},
                {"data": "leagueName"},
                {"data": null}
            ],
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                return nRow;
            },
        });
    }
};

var group = {

    userPrediction : function(matchId, predictions) {
        console.log('prediction for ' + matchId);
        for (pred in predictions) {
            var item = predictions[pred];
            console.log(item);

            if(matchId == item.match.id){
                console.log("match!");
                return item;
            }
        }

    },
    showGroup : function (groupName){
        main.hideAllTags();
        $("#tag-group").show();
        console.log("show group " + groupName);
        main.makeAjaxCall("/rest/group/"+groupName, "GET")
            .done(function(groupResponse){
                var predictions = [];
                main.makeAjaxCall("/rest/user/prediction/"+groupName, "GET")
                    .done(function(response){
                        console.log(response);
                        predictions = response;

                    main.session.set("group", groupResponse);
                    var standings = groupResponse.standings;
                    for(var i = 0; i < standings.length;  i++) {
                        var item = standings[i];
                        console.log(item);
                        console.log(standings);
                        $("#group-table-team-"+(i)).html(item.team);
                        if (item.goalsFor) {
                            $("#group-table-goalsFor-"+(i)).html(item.goalsFor);
                            $("#group-table-goalsAgains-"+(i)).html(item.goalsAgainst);
                            $("#group-table-goalDifference-"+(i)).html(item.goalsFor - item.goalsAgainst);
                        }


                        $("#group-table-points-"+(i)).html(item.points);
                    }

                    for(var m = 0; m < groupResponse.matches.length; m++){
                        var match = groupResponse.matches[m];
                        $("#group-match-date-"+(m)).html($.format.date(new Date(match.kickoff), 'dd MMM HH:mm'));
                        $("#group-match-home-"+(m)).html(match.home);
                        $("#group-match-away-"+(m)).html(match.away);

                        var predicion = group.userPrediction(match.id, predictions);
                        if(predicion){
                            $("#homeGoals"+(m)).val(predicion.homeGoals);
                            $("#awayGoals"+(m)).val(predicion.awayGoals);
                        } else {
                            console.log("no prediction");
                            $("#homeGoals"+(m)).val("");
                            $("#awayGoals"+(m)).val("");

                        }

                    }
                });

            })
    }

    
}


$(document).ready(function() {
    main.hideAllTags();
    if(!main.session.get("user")){
        menu.userLoggedOut();
    } else {
        menu.userLoggedIn();
    }


});
