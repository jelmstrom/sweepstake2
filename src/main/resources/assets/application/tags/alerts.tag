<alerts>
    <div class="row">
        <div class="col-xs-8 col-xs-offset-2" style="height: 60px">
            <div class="alert alert-info" id="alert-info" role="alert">
            </div>
            <div class="alert alert-warning" id="alert-warning" role="alert">
            </div>
            <div class="alert alert-danger" id="alert-fail" role="alert">
            </div>
        </div>
    </div>

    <script>
        this.on('mount', function () {
            main.hideAlerts();
        })
    </script>

</alerts>