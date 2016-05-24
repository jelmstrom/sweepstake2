<group>
    <div id="div-group">
        <div class="row">
            <div class="col-xs-12 col-md-10 col-md-offset-1">
                <div class="col-xs-6 col-md-4" id="group-actual-standings">
                    <table class="table ">
                        <thead>
                            <tr>
                                <th width="5%"></th>
                                <th width="65%">Team</th>
                                <th width="5%">+</th>
                                <th width="5%">-</th>
                                <th width="5%">+/-</th>
                                <th width="15%">points</th>
                            </tr>
                        </thead>
                        <tbody id="groupTable">
                        <tr>
                            <td>1.</td>
                            <td><span id="group-table-team-0"></span></td>
                            <td><span id="group-table-foalsFor-0"></span></td>
                            <td><span id="group-table-goalsAgainst-0"></span>  </td>
                            <td><span id="group-table-goalDiff-0"></span> </td>
                            <td><span id="group-table-points-0"></span> </td>
                        </tr>
                        <tr>
                            <td>2.</td>
                            <td><span id="group-table-team-1"></span></td>
                            <td><span id="group-table-foalsFor-1"></span></td>
                            <td><span id="group-table-goalsAgainst-1"></span>  </td>
                            <td><span id="group-table-goalDiff-1"></span> </td>
                            <td><span id="group-table-points-1"></span> </td>
                        </tr>
                        <tr>
                            <td>3.</td>
                            <td><span id="group-table-team-2"></span></td>
                            <td><span id="group-table-foalsFor-2"></span></td>
                            <td><span id="group-table-goalsAgainst-2"></span>  </td>
                            <td><span id="group-table-goalDiff-2"></span> </td>
                            <td><span id="group-table-points-2"></span> </td>
                        </tr>
                        <tr>
                            <td>4.</td>
                            <td><span id="group-table-team-3"></span></td>
                            <td><span id="group-table-foalsFor-3"></span></td>
                            <td><span id="group-table-goalsAgainst-3"></span>  </td>
                            <td><span id="group-table-goalDiff-3"></span> </td>
                            <td><span id="group-table-points-3"></span> </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="col-xs-6 col-md-4 col-md-offset-1" id="group-predicted-outcome">
                    <form role="form" id="frm-predicted-table">
                        <div class="form-group">
                            <input type="text" name="username" id="position1" class="form-control input-sm" placeholder="Position 1" required>
                        </div>
                        <div class="form-group">
                            <input type="text" name="username" id="position2" class="form-control input_sm" placeholder="Position 2" required>
                        </div>
                        <div class="form-group">
                            <input type="text" name="username" id="position3" class="form-control input-sm" placeholder="Position 3" required>
                        </div>
                        <div class="form-group">
                            <input type="text" name="username" id="position4" class="form-control input-sm" placeholder="Position 4" required>
                        </div>
                        <input type="submit" value="Submit" id="btn-predicted-table" class="btn btn-md btn-success btn-block">
                    </form>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-md-10 col-md-offset-1">
               <table class="table">
                   <thead>
                        <tr>
                            <th class="col-xs-2">Date</th>
                            <th class="col-xs-4">Home team</th>
                            <th class="col-xs-4">Away team</th>
                            <th class="col-xs-1"></th>
                            <th class="col-xs-1"></th>
                        </tr>
                   </thead>
                   <tbody id="group-matches">
                   <tr>
                       <td id="group-match-date-0">date</td>
                       <td id="group-match-home-0">a</td>
                       <td id="group-match-away-0">a</td>
                       <td id="group-match-homeGoals-0"><input type="number" min="0" class="input-sm" id="homeGoals0" /></td>
                       <td id="group-match-awayGoals-0"><input type="number" min="0" class="input-sm" id="awayGoals0" /></td>
                   </tr>
                   <tr>
                       <td id="group-match-date-1">date</td>
                       <td id="group-match-home-1">a</td>
                       <td id="group-match-away-1">a</td>
                       <td id="group-match-homeGoals-1"><input type="number" min="0" class="input-sm" id="homeGoals1" /></td>
                       <td id="group-match-awayGoals-1"><input type="number" min="0" class="input-sm" id="awayGoals1" /></td>
                   </tr>
                   <tr>
                       <td id="group-match-date-2">date</td>
                       <td id="group-match-home-2">a</td>
                       <td id="group-match-away-2">a</td>
                       <td id="group-match-homeGoals-2"><input type="number" min="0" class="input-sm" id="homeGoals2" /></td>
                       <td id="group-match-awayGoals-2"><input type="number" min="0" class="input-sm" id="awayGoals2" /></td>
                   </tr>
                   <tr>
                       <td id="group-match-date-3">date</td>
                       <td id="group-match-home-3">a</td>
                       <td id="group-match-away-3">a</td>
                       <td id="group-match-homeGoals-3"><input type="number" min="0" class="input-sm" id="homeGoals3" /></td>
                       <td id="group-match-awayGoals-3"><input type="number" min="0" class="input-sm" id="awayGoals3" /></td>
                   </tr>
                   <tr>
                       <td id="group-match-date-4">date</td>
                       <td id="group-match-home-4">a</td>
                       <td id="group-match-away-4">a</td>
                       <td id="group-match-homeGoals-4"><input type="number" min="0" class="input-sm" id="homeGoals4" /></td>
                       <td id="group-match-awayGoals-4"><input type="number" min="0" class="input-sm" id="awayGoals4" /></td>
                   </tr>
                   <tr>
                       <td id="group-match-date-5">date</td>
                       <td id="group-match-home-5">a</td>
                       <td id="group-match-away-5">a</td>
                       <td id="group-match-homeGoals-5"><input type="number" min="0" class="input-sm" id="homeGoals5" /></td>
                       <td id="group-match-awayGoals-5"><input type="number" min="0" class="input-sm" id="awayGoals5" /></td>
                   </tr>
                   <tr>
                       <td colspan="3"></td>
                       <td colspan="2"><button class="btn btn-success" id="btn-group-submit-result">Submit</button></td>
                   </tr>

                   </tbody>
               </table>

            </div>
                <!-- matches -->
        </div>
    </div>

    <script>
        this.on('mount', function () {
            $("#btn-group-submit-result").on("click", function(event){
                event.preventDefault();
                var group = main.group();
                var user = main.user();
                var predictions =
                    [
                        {
                            user: user,
                            match: group.matches[0],
                            homeGoals: $("#homeGoals1").val(),
                            awayGoals: $("#awayGoals1").val(),
                        },
                        {
                            user: user,
                            match: group.matches[1],
                            homeGoals: $("#homeGoals2").val(),
                            awayGoals: $("#awayGoals2").val(),
                        },
                        {
                            user: user,
                            match: group.matches[2],
                            homeGoals: $("#homeGoals3").val(),
                            awayGoals: $("#awayGoals3").val(),
                        },
                        {
                            user: user,
                            match: group.matches[3],
                            homeGoals: $("#homeGoals4").val(),
                            awayGoals: $("#awayGoals4").val(),
                        },
                        {
                            user: user,
                            match: group.matches[5],
                            homeGoals: $("#homeGoals5").val(),
                            awayGoals: $("#awayGoals5").val(),
                        },
                        {
                            user: user,
                            match: group.matches[5],
                            homeGoals: $("#homeGoals6").val(),
                            awayGoals: $("#awayGoals6").val(),
                        }
                    ];

                console.log(predictions);


            })
        });
        this.on('update', function () {
            console.log('update');
        });
        this.on('*', function(eventName) {
            console.info(eventName)
        })

    </script>

</group>