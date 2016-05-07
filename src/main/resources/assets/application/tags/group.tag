<group>
    <div id="div-group">
        <div class="row">
            <div class="col-xs-12 col-md-10 col-md-offset-1">
                <div class="col-xs-6 col-md-4" id="group-actual-standings">
                    <table class="table ">
                        <thead>
                            <tr>
                                <th width="50%">Team</th>
                                <th width="5%">+</th>
                                <th width="5%">-</th>
                                <th width="5%">+/-</th>
                                <th width="35%">points</th>
                            </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td> hasdkahd</td>
                            <td> 10 </td>
                            <td> 1 </td>
                            <td> 9 </td>
                            <td> 9 </td>
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