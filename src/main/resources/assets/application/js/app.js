var main = {
    dev : true,
    ns : null,
    session : null,
    hideAllTags : function (){
        $("#tag-register").hide();
        $("#tag-user").hide();
    },
    user : function(){
        return main.session.get("user");
    },

    makeAjaxCall : function (url, type, data) {
            var deferredObject = $.Deferred();
            var msgBody;
            if(data){
                msgBody=JSON.stringify(data);
            }
            if(main.dev){
                url = 'http://localhost:8081'+url
            }
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
        $("#menu-register").on("click", function() {
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
            $("#userinfo-isadmin").text(main.user().isAdmin);
            $("#userinfo-username").text(main.user().username);
            var leagues = $('#userinfo-league-table').DataTable();
            leagues.data().draw();
            
            $("#tag-user").show();
        });
    }
};


$(document).ready(function() {
    main.hideAllTags();
    if(!main.session.get("user")){
        menu.userLoggedOut();
    } else {
        menu.userLoggedIn();
    }
    $("#header").html("Sweepstake");
    $("#tag-register").hide();

});
