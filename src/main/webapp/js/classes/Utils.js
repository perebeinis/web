var Utils = {
    checkFieldEnabled: function checkFieldEnabled(fieldData) {
        var enable = false;
        if (fieldData.enableParams) {
            var enableParams = fieldData.enableParams;
            for (var i in enableParams) {
                var parameter = enableParams[i];
                if (parameter.mode && Utils.getUrlParameter("mode") == parameter.mode) {
                    enable = true;
                    break;
                }

            }
        }

        return enable;
    },


    getUrlParameter: function getUrlParameter(k) {
        var p = {};
        location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (s, k, v) {
            p[k] = v
        })
        return k ? p[k] : p;
    },

    checkIfHasEmptyFiles: function checkIfHasEmptyFiles(className){
        var hasEmptyFiles = false;
        $("."+className+" input[type=file]").each(function (data) {
            if(this.files.length == 0){
                hasEmptyFiles = true;
            }
        });
        return hasEmptyFiles;
    },

    countEmptyFiles: function countEmptyFiles(className){
        var hasEmptyFiles = 0;
        $("."+className+" input[type=file]").each(function (data) {
            if(this.files.length == 0){
                hasEmptyFiles++;
            }
        });
        return hasEmptyFiles;
    }
}