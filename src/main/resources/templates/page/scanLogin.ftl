<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>授权登陆</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <meta name="description" content="Write an awesome description for your new site here. You can edit this line in _config.yml. It will appear in your document head meta (for Google search results) and in your feed.xml site description.
">
    <link rel="stylesheet" href="/demo/lib/weui.min.css">
    <link rel="stylesheet" href="/demo/css/jquery-weui.css">
    <link rel="stylesheet" href="/demo/css/style.css">
    <script src="/demo/js/jquery.min.js"></script>
</head>
<body ontouchstart>
<script type="text/javascript">
</script>
<div class="weui-msg">
    <div class="weui-msg__icon-area"><i class="weui-icon-waiting weui-icon_msg"></i></div>
    <div class="weui-msg__text-area">
        <h2 class="weui-msg__title">编辑器授权登陆</h2>
        <p class="weui-msg__desc">您必须进行登陆才可编辑发表您的文章
            <br>请遵循我们的规定-谢谢合作
            <br>Tips:请勿刷新本页面</p>
    </div>
    <div class="weui-msg__opr-area">
        <p class="weui-btn-area">
            <a onclick="startLogin('1')" class="weui-btn weui-btn_primary">授权登陆</a>
            <a onclick="startLogin('0')" class="weui-btn weui-btn_default">取消授权</a>
        </p>
    </div>
    <div class="weui-msg__extra-area">
        <div class="weui-footer">
            <p class="weui-footer__text">Copyright © 来自楠尼玛的授权登陆</p>
        </div>
    </div>
</div>
<script type="text/javascript">
    function startLogin(action) {
        $.get("${projectUrl}/webQcodeLogin?websocketSessionId=${websocketSessionId}&openid=${openid}&actions="+action, function(result) {
                        if(result=="success"){
                            alert("恭喜你成功登陆！");
                            WeixinJSBridge.call('closeWindow');
                        }
                        else{
                            WeixinJSBridge.call('closeWindow');
                        }
        });
    }
</script>
</body>

</html>