function UserAssocComponent(){
}

UserAssocComponent.prototype.userAssoc = function(data, parentElementId, elementValue) {
    var scope = this;
    var maxElements = data.maxElements != undefined ? data.maxElements : 100;
    var resultTable = ["firstName", "lastName"];
    $('#' + parentElementId)
        .append($('<div>', {class: "textField" + " " + data[this.customClassName]})
            .append($('<span>', {class: "hidden popup"})
                .html(this.messages["mandatory"]))
            .append($('<label>', {value: data[this.title]})
                .html(this.messages[data[this.title]])).append($('<input>', {
                maxElements: maxElements,
                class: "form-control " + data[this.name], name: data[this.name],
                placeholder: "search ...",
                customType: data[this.typeForSaving] ? data[this.typeForSaving] : "",
                type: 'text', value: "",
                readonly: !Utils.checkFieldEnabled(data)
            }).keyup(function (event) {
                scope.searchUsers(event, this);
            })).append($('<table>', {class: "added"})).append($('<table>', {class: "searchResult"})));

    if (data[this.mandatoryCondition] != "") {
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }

    if (!Utils.checkFieldEnabled(data)) {
        var addedTable = $('#' + parentElementId).find('table.added')[0];
        for (var index in elementValue) {
            var searchResultElement = elementValue[index];
            $(addedTable).append($('<tr>', {id: searchResultElement._id["$oid"]}).append(
                $.map(resultTable, function (elementName, index) {
                    return '<td>' + searchResultElement[elementName] + '</td>';
                }).join()
            ));
        }
    }
}

UserAssocComponent.prototype.searchUsers = function(event, data) {
    var resultTable = ["firstName", "lastName"];
    var table = $(data.parentNode).find('table.searchResult')[0];
    var addedTable = $(data.parentNode).find('table.added')[0];
    table.innerHTML = "";
    var searchValue = data.value;
    var userAssocInput = data;
    var scope = this;
    var maxElements = parseInt(data.attributes.maxelements.nodeValue);
    if (data.value != "") {
        // create headers
        /*
         var headerTR = $(table).append($('<tr>', {class: "header"}).append(
         $.map(resultTable, function (elementName, index) {
         return '<th>' + CUSTOM_MESSAGES["user.card." + elementName] + '</th>';
         }).join()
         ));
         */

        var search = {};
        var searchParams = {};
        searchParams.searchType = "user";
        searchParams.searchByOr = true;
        searchParams.searchData = {};
        for (var index in resultTable) {
            searchParams.searchData[resultTable[index]] = searchValue;
        }
        search.search = searchParams;
        search.draw = 1;
        search.length = 100;
        search.start = 0;

        $.ajax({
            url: '/search-data',
            type: "POST",
            contentType: "application/json",
            data: encodeURI(JSON.stringify(search)),
            dataType: 'json'
        }).done(function (data) {
            if (data.data.length > 0) {
                for (var index in data.data) {
                    var searchResultElement = data.data[index];
                    $(table).append($('<tr>', {id: searchResultElement._id["$oid"]}).append(
                        $.map(resultTable, function (elementName, index) {
                            return '<td>' + searchResultElement[elementName] + '</td>';
                        }).join()
                    ).click(function () {
                        var addedRows = $(addedTable).find("tr").length;
                        if (addedRows < maxElements && $(addedTable).find("#" + this.id).length == 0) {
                            $(addedTable).append($(this.outerHTML).click(function () {
                                this.parentNode.removeChild(this);
                            }));
                            userAssocInput.value = "";
                            scope.searchUsers(event, userAssocInput);
                        }
                    }));
                }
            }
            console.log("done");
        });
    }

    console.log("aaa");
}
