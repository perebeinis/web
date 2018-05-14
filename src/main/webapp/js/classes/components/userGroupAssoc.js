function UserGroupAssocComponent() {
}

UserGroupAssocComponent.prototype.userGroupAssoc = function (data, parentElementId, elementValue) {
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
                class: "form-control userAssoc " + data[this.name], name: data[this.name],
                placeholder: "search ...",
                customType: data[this.typeForSaving] ? data[this.typeForSaving] : "",
                type: 'text', value: "",
                readonly: !Utils.checkFieldEnabled(data)
            }).keyup(function (event) {
                scope.searchUsers(event, this);
            }))
            .append($('<input>', {
                type: 'button',
                class: "checkAllUsersInGroup",
                value: "checkAllUsersInGroup"
            }).click(this, function (e, data) {
                var searchData = {};
                searchData.parentNode = this.parentNode;
                searchData.value = "*";
                searchData.checkAllUsersInGroup = true;
                scope.searchUsers(e, searchData);
            }))

            .append($('<table>', {class: "added"})).append($('<table>', {class: "searchResult"})));

    if (data[this.mandatoryCondition] != "") {
        this.mandatoryCondtitions[data[this.name]] = data[this.mandatoryCondition];
    }

    if (!Utils.checkFieldEnabled(data)) {
        var addedTable = $('#' + parentElementId).find('table.added')[0];
        for (var index in elementValue) {
            var searchResultElement = elementValue[index];
            $(addedTable).append($('<tr>', {id: searchResultElement["$oid"]}).append(
                $.map(resultTable, function (elementName, index) {
                    return '<td>' + searchResultElement[elementName] + '</td>';
                }).join()
            ));
        }
    }
}

UserGroupAssocComponent.prototype.searchUsers = function (event, data) {
    var resultTable = ["firstName", "lastName"];
    var table = $(data.parentNode).find('table.searchResult')[0];
    var addedTable = $(data.parentNode).find('table.added')[0];
    table.innerHTML = "";
    var searchValue = data.value.replace("*", "");
    var checkAllUsersInGroup = data.checkAllUsersInGroup;
    var userAssocInput = data;
    var scope = this;
    var maxElements = data.attributes ? parseInt(data.attributes.maxelements.nodeValue) : 500;
    if (data.value != "") {

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
                    if (checkAllUsersInGroup) {
                        if ($(addedTable).find("#" + searchResultElement._id["$oid"]).length == 0) {
                            $(addedTable).append(
                                $('<tr>',
                                    {id: searchResultElement._id["$oid"]})
                                    .append(
                                        $.map(resultTable, function (elementName, index) {
                                            return '<td>' + searchResultElement[elementName] + '</td>';
                                        }).join()
                                    ).click(function () {
                                         this.parentNode.removeChild(this);
                                }));
                        }
                        userAssocInput.value = "";
                        scope.searchUsers(event, userAssocInput);
                    }else {
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
            }
        });
    }
}
