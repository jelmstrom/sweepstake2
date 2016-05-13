<navbar>
    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <div class="col-xs-10 col-sm-9 col-md-7">

                    <ul class="nav navbar-nav" id="navbar-groups">
                        <!-- disable tabs that operate on a logger when there is no logger connected -->
                        <li class="navitem" id="menuGroupA" onClick="javascript: group.showGroup('A')"><a href="#">Group A</a></li>
                        <li class="navitem" id="menuGroupB" onClick="javascript: group.showGroup('B')"><a href="#">Group B</a></li>
                        <li class="navitem" id="menuGroupC" onClick="javascript: group.showGroup('C')"><a href="#">Group C</a></li>
                        <li class="navitem" id="menuGroupD" onClick="javascript: group.showGroup('D')"><a href="#">Group D</a></li>
                        <li class="navitem" id="menuGroupE" onClick="javascript: group.showGroup('E')"><a href="#">Group E</a></li>
                        <li class="navitem" id="menuGroupF" onClick="javascript: group.showGroup('F')"><a href="#">Group F</a></li>
                    </ul>
                    <ul class="nav navbar-nav" id="navbar-playoff">
                        <li class="navitem" id="menuPlayoff"><a href="#">Playoff</a></li>
                    </ul>
                </div>
                <!-- download alerts -->
                <div class="navbar-right col-xs-2 col-sm-3 col-md-5" id="alert-panel">
                    <ul class="nav navbar-nav">
                        <li class="dropdown" id="menu-user">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                                <span id="p-user">user</span>
                                <span class="caret"> </span>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="#" id="menu-logout">logout</a></li>
                                <li><a href="#" id="menu-details">Details</a></li>
                            </ul>
                        </li>
                        <li class="navitem" id="menu-register"><a href="#">Register</a></li>
                    </ul>
                </div>
            </div>
            <!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>

</navbar>