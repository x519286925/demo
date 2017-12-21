<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>管理员审核界面--${power}</title>
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
</script>
<a class="weui-btn weui-btn_disabled weui-btn_primary" style="color: white">待审核文章</a>
<div class="weui-panel weui-panel_access">

    <div class="weui-panel__bd">
    <#list informationList as information>
        <a href="#" class="weui-media-box weui-media-box_appmsg" id="show-actions_${information_index}" onclick="menuDisplayChose('${information_index}')">
            <input hidden  value="${information.id}" id="id_${information_index}"/>
            <div class="weui-media-box__hd">
                <img class="weui-media-box__thumb" src="${information.photo}">
            </div>
            <div class="weui-media-box__bd">
                <h4 class="weui-media-box__title">${information.title}</h4>
                <p class="weui-media-box__desc">${information.description}</p>
            </div>
        </a>
        </#list>
        <div id="pageContent"></div>
        <#if "${pageContent}"=="true">
            <a class="weui-btn weui-btn_default" id="reloadManyContent">点击加载更多</a>
        </#if>
        <input value="${size}" hidden id="size">
        <input value="${page}" hidden id="page">
        <input value="${informationList?size}" hidden id="inforlistNumber">
        <input value="${projectUrl}" hidden id="projectUrl">
        <script>
            $("#reloadManyContent").click(function() {
                document.getElementById("reloadManyContent").innerHTML="...请稍后，正在加载中...";
                document.getElementById("reloadManyContent").id="none";   //防止重复点击
                var size =  document.getElementById("size").value;
                var page =  document.getElementById("page").value;
                $.get("/demo/managerIndexPage?size="+size+"&page="+(page-0+1), function (data, status) {
                    var eventList = data.informationList;   //文章内容集合
                    var allcontent = "";   //获得全部内容HTML
                    for(var i = 0; i<eventList.length;i++){
                        var inforlistNumber = document.getElementById("inforlistNumber").value-0+1;
                        document.getElementById("inforlistNumber").value=inforlistNumber;
                        allcontent = allcontent+
                                '        <a href="#" class="weui-media-box weui-media-box_appmsg" id="show-actions_'+inforlistNumber+'" onclick="menuDisplayChose(\''+inforlistNumber+'\')">'+
                                '            <input hidden  value="'+eventList[i].id+'" id="id_'+inforlistNumber+'"/>'+
                                '            <div class="weui-media-box__hd">'+
                                '                <img class="weui-media-box__thumb" src="'+eventList[i].photo+'">'+
                                '            </div>'+
                                '            <div class="weui-media-box__bd">'+
                                '                <h4 class="weui-media-box__title">'+eventList[i].title+'</h4>'+
                                '                <p class="weui-media-box__desc">'+eventList[i].description+'</p>'+
                                '            </div>'+
                                '        </a>';
                    }
                    document.getElementById("size").value = data.size;
                    document.getElementById("page").value = data.page;
                    var content =  document.getElementById("pageContent").innerHTML;   //取出原本盒子的内容；
                    document.getElementById("pageContent").innerHTML = content+allcontent;  //重新放回去加上新的内容
                    if(data.pageContent=="true"){
                        document.getElementById("none").id="reloadManyContent";
                        document.getElementById("reloadManyContent").innerHTML="点击加载更多";

                    }
                    else{
                        document.getElementById("none").innerHTML="----已无更多内容----";

                    }
                });
            });
            function menuDisplayChose(id) {
              var newsId =  document.getElementById("id_"+id).value;  //文章id
                $.actions({
                    title: "选择操作",
                    onClose: function () {
                        console.log("close");
                    },
                    actions: [
                        {
                            text: "查看详情",
                            className: "color-primary",
                            onClick: function () {
                                window.location.href = "${projectUrl}/managerCheckNews?id="+newsId;
                            }
                        },
                        {
                            text: "审核通过",
                            className: "color-warning",
                            onClick: function () {
                                $.get("${projectUrl}/managerforNews?id="+newsId+"&action=0", function (result) {
                                    if (result == "success") {
                                        var list = document.getElementById("show-actions_"+id);
                                        list.parentNode.removeChild(list);
                                        $.alert("已审核通过");
                                    } else {
                                        $.alert("审核不通过");
                                    }
                                });
                            }
                        },
                        {
                            text: "禁止通过",
                            className: 'color-danger',
                            onClick: function () {
                                $.get("${projectUrl}/managerforNews?id="+newsId+"&action=2", function (result) {
                                    if (result == "not_success") {
                                        var list = document.getElementById("show-actions_"+id);
                                        list.parentNode.removeChild(list);
                                        $.alert("已提交操作");
                                    } else {
                                        $.alert("操作失败");
                                    }
                                });
                            }
                        }
                    ]
                });
            }
        </script>
    </div>
</div>
</body>

</html>