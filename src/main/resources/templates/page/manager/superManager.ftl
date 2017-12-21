<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>管理员界面</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <meta name="description" content="Write an awesome description for your new site here. You can edit this line in _config.yml. It will appear in your document head meta (for Google search results) and in your feed.xml site description.
">

    <link rel="stylesheet" href="/demo/lib/weui.min.css">
    <link rel="stylesheet" href="/demo/css/jquery-weui.css">
    <script src="/demo/js/jquery.min.js"></script>
    <script src="/demo/js/jquery-weui.js"></script>
</head>
<body ontouchstart>
<input type="hidden" id="refreshed" value="no">
<script type="text/javascript">
    window.onload = function() {
        var e = document.getElementById("refreshed");
        if (e.value == "no")
            e.value = "yes";
        else {
            e.value = "no";
            location.reload();
        }
    }
    function getRole(openid,id){
        $.get("${projectUrl}/getRoleName?openid="+openid, function (result) {
                document.getElementById("role_"+id).innerHTML =result;
        });
    }
</script>
<a class="weui-btn weui-btn_disabled weui-btn_primary" style="color: white">用户列表</a>
<div class="weui-panel weui-panel_access">
    <div class="weui-panel__bd">
    <#list userList as user>
        <a href="#" class="weui-media-box weui-media-box_appmsg" id="show-actions_${user_index}" onclick="menuDisplayChose('${user_index}')">
            <div class="weui-media-box__hd">
                <img class="weui-media-box__thumb" src="${user.headImage}">
            </div>
            <input hidden  value="${user.openId}" id="id_${user_index}"/>
            <div class="weui-media-box__bd">
                <h4 class="weui-media-box__title">${user.nickName}</h4>
                <p class="weui-media-box__desc" id = "role_${user_index}"></p>
                <script type="text/javascript">
                    getRole('${user.openId}','${user_index}');
                </script>
            </div>
        </a>
    </#list>
        <script>
            if('${power}'==1){
                function menuDisplayChose(id) {
                    var openid = document.getElementById("id_"+id).value;
                    $.actions({
                        title: "选择操作",
                        onClose: function () {
                            console.log("close");
                        },
                        actions: [
                            {
                                text: "设为管理员",
                                className: "color-primary",
                                onClick: function () {
                                    $.get("${projectUrl}/managerforPower?openid="+openid+"&power=2", function (result) {
                                        if(result.indexOf("操作失败")>=0){
                                            $.alert(result);
                                        }
                                        else{
                                            $.alert(result);
                                            var openid =   document.getElementById("id_"+id).value;
                                            getRole(openid,id);
                                        }
                                    });
                                }
                            },
                            {
                                text: "设为普通用户",
                                className: "color-warning",
                                onClick: function () {
                                    $.get("${projectUrl}/managerforPower?openid="+openid+"&power=0", function (result) {
                                        if(result.indexOf("操作失败")>=0){
                                            $.alert(result);
                                        }
                                        else{
                                            $.alert(result);
                                            var openid =   document.getElementById("id_"+id).value;
                                            getRole(openid,id);
                                        }
                                    });
                                }
                            },
                            {
                                text: "拉黑用户",
                                className: 'color-danger',
                                onClick: function () {
                                    $.alert("未开发");
                                }
                            }
                        ]
                    });
                }
            }
            else {
                function menuDisplayChose(id) {
                    var openid = document.getElementById("id_" + id).value;
                    $.actions({
                        title: "选择操作",
                        onClose: function () {
                            console.log("close");
                        },
                        actions: [
                            {
                                text: "拉黑用户",
                                className: 'color-danger',
                                onClick: function () {
                                   $.alert("未开发");
                                }
                            }
                        ]
                    });
                }
            }
        </script>
    </div>
</div>
</body>

</html>