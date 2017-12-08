function SearchDataComponent(tableDivId, data) {
    this.tableDivId = tableDivId;
    this.customClassName = "customClassName";
    this.title = "title";
    this.name = "name";
    this.type = "type";
    this.mandatoryCondition = "mandatoryCondition";


    this.searchData = function () {
        var searchType = {};
        searchType["searchType"] = "user";

        var searchData = {};
        searchData["aaa"] = "trs";

        searchType["searchData"] = searchData;
        console.log("serchhhhhhhhhhhhhhhhhh");

        var scope = this;
        $.ajax({
            url: '/search-data',
            type: "POST",
            contentType: "application/json",
            data:  JSON.stringify(searchType),
            success: function(response) {
                scope.createSearchTable(response);
            },
            dataType: 'json'
        });
    }

    this.createSearchTable = function (result) {


        $(document).ready(function(){
            var data =result;
            var table = $("#"+tableDivId).DataTable( {
                "aaData": data,
                "aoColumns": [
                    { "mData": "firstName"},
                    { "mData": "lastName"},
                    { "mData": "user_id"}
                ],
                "paging":true,
                "pageLength":20,
                "ordering":true,
                "order":[0,"asc"]
            });
        });

        for (var i in result){
            var data = result[i];
            console.log(data);

            /*
            $('#'+tableDivId)
                .append($('<div>', {class: " navbar-header "+" "+name}).
                append($('<a>' ,{class: "navbar-brand "}).click(this[name]).html(set[this.title])));

            */

        }
    }



    return this;
};
