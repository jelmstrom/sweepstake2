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
                            <td><span id="group-table-team-1"></span></td>
                            <td><span id="group-table-foalsFor-1"></span></td>
                            <td><span id="group-table-goalsAgainst-1"></span>  </td>
                            <td><span id="group-table-goalDiff-1"></span> </td>
                            <td><span id="group-table-points-1"></span> </td>
                        </tr>
                        <tr>
                            <td>2.</td>
                            <td><span id="group-table-team-2"></span></td>
                            <td><span id="group-table-foalsFor-2"></span></td>
                            <td><span id="group-table-goalsAgainst-2"></span>  </td>
                            <td><span id="group-table-goalDiff-2"></span> </td>
                            <td><span id="group-table-points-2"></span> </td>
                        </tr>
                        <tr>
                            <td>3.</td>
                            <td><span id="group-table-team-3"></span></td>
                            <td><span id="group-table-foalsFor-3"></span></td>
                            <td><span id="group-table-goalsAgainst-3"></span>  </td>
                            <td><span id="group-table-goalDiff-3"></span> </td>
                            <td><span id="group-table-points-3"></span> </td>
                        </tr>
                        <tr>
                            <td>4.</td>
                            <td><span id="group-table-team-4"></span></td>
                            <td><span id="group-table-foalsFor-4"></span></td>
                            <td><span id="group-table-goalsAgainst-4"></span>  </td>
                            <td><span id="group-table-goalDiff-4"></span> </td>
                            <td><span id="group-table-points-4"></span> </td>
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
            </div>
                <!-- matches -->
        </div>
    </div>

    <script>
        this.on('mount', function () {
        });
        this.on('update', function () {
            console.log('update');
        });
        this.on('*', function(eventName) {
            console.info(eventName)
        })

    </script>

</group>