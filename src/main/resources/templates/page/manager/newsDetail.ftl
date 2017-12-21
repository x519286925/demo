<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>管理员审核界面--文章详情</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <meta name="description" content="Write an awesome description for your new site here. You can edit this line in _config.yml. It will appear in your document head meta (for Google search results) and in your feed.xml site description.
">
    <link rel="stylesheet" href="/demo/lib/weui.min.css">
    <link rel="stylesheet" href="/demo/css/jquery-weui.css">
    <script src="/demo/js/jquery.min.js"></script>
    <script src="/demo/js/jquery-weui.js"></script>
</head>
<body ontouchstart>

<h2 style="text-align: center">${information.title}</h2>
${information.content}
<div style="text-align: center">
</div>
<a href="#" class="weui-btn weui-btn_primary" style="width: 100%" id="newForYes">审核通过</a>
<a href="#" class="weui-btn weui-btn_warn" style="width: 100%" id="newForNo">审核不通过</a>
</body>
<script>
    document.getElementById("newForYes").onclick = function(){
        $.get("${projectUrl}/managerforNews?id=${information.id}&action=0", function(result) {
            if(result=="success"){
                $.alert("已审核通过");
            }else{
                $.alert("操作失败");
            }
        });
    }
    document.getElementById("newForNo").onclick = function(){
    $.get("${projectUrl}/managerforNews?id=${information.id}&action=2", function (result) {
        if (result == "not_success") {
            $.alert("已提交操作");
        } else {
            $.alert("操作失败");
        }
    });
}
</script>
</html>