<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>summernote编辑器</title>

    <link rel="stylesheet" type="text/css" href="http://apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.css">
    <!--bootstrap-->
    <#--<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>-->
    <link href="/demo/dist/summernote.css" rel="stylesheet"/>
    <script src="/demo/js/jquery.min.js"></script>
    <script src="/demo/js/bootstrap.min.js"></script>
    <script src="/demo/dist/summernote.js"></script>
    <script src="/demo/dist/lang/summernote-zh-CN.js"></script>    <!-- 中文-->

    <#--预览文档CSS-->
    <link rel="stylesheet" type="text/css" href="/demo/css/normalize.css" />
    <link rel="stylesheet" type="text/css" href="/demo/css/demo.css">
    <link rel="stylesheet" type="text/css" href="/demo/css/sheets-of-paper-a4.css">



    <style>
        .m{ width: 48%; margin-left: 30px;float: left;margin-top: 37px; }
        .n{ width: 48%; float: left;margin-left: 20px }
        @media (max-width: 1272px) {
            .m{ width: 98%; margin-left: 10px;margin-right: 10px;margin-top: 37px;}
            .n{ width: 98%; margin-left: 10px;margin-right: 10px;}
        }
        .displayLabel{
            cursor:not-allowed;
        }
        .loading{
               width:325px;
               height:56px;
               position:absolute;
               top:50%;
               left: 25%;
               line-height:56px;
               color:#fff;
               padding-left:60px;
               font-size:15px;
               background: #000 url(images/loader.gif) no-repeat 10px 50%;
               opacity: 0.7;
               z-index:9999;
               -moz-border-radius:20px;
               -webkit-border-radius:20px;
               border-radius:20px;
               filter:progid:DXImageTransform.Microsoft.Alpha(opacity=70);
           }
    </style>

    <script>
        $(function(){
            $('.summernote').summernote({
                height: 600,
                tabsize: 2,
                lang: 'zh-CN',
                focus:true,
                toolbar: [
                    ['style', ['style']],
                    ['font', ['bold', 'underline', 'clear']],
                    ['fontname', ['fontname']],
                    ['color', ['color']],
                    ['para', ['ul', 'ol', 'paragraph']],
                    ['table', ['table']],
                    ['insert', ['link', 'picture', 'video']],
                    ['view', ['fullscreen', 'codeview', 'help']]
                ],
                callbacks: {
                    onImageUpload: function (files) { //the onImageUpload API
                        img = sendFile(files[0]);
                    }
                }
            });


        });
        function sendFile(file) {
            document.getElementById("loading5").style.display="block";
            data = new FormData();
            data.append("file", file);
            $.ajax({
                data: data,
                type: "POST",
                url: "${projectUrl}/testuploadimg2",
                cache: false,
                contentType: false,
                processData: false,
                success: function(data) {
                    if("uploadFail"==data){
                        alert("插入失败");
                        return;
                    }
                    $("#nanSummernote").summernote('insertImage', data, 'image name'); // the insertImage API
                    document.getElementById("loading5").style.display="none";
                },
            });
        }
            function displayNews() {    //实时更新
                var content = $('#nanSummernote').summernote('code');
                document.getElementById("displayWord").innerHTML=content;
            }
            function saveNews(){
                var title = document.getElementById("title").value;
                var content = $('#nanSummernote').summernote('code');
                if(title.length<1){
                    alert("请给你的表演给个标题");
                    return;
                }
                if(content.length<1){
                    alert("你的表演内容不能为空");
                    return;
                }
                //以上为简单的校验前端校验
                $('#myModal').modal({    //弹出框
                });

            }
            function submitNews(){   //提交你的表演
                var title = document.getElementById("title").value;   //标题
                var content = $('#nanSummernote').summernote('code');   //内容
                var photo = document.getElementById("fileUploadUrl").value;  //缩略图链接
                var description = document.getElementById("comment").value;  //描述
                var type = document.getElementById("alldisplayLabel").value;  //所有标签
                var newsId = document.getElementById("newsId").value; //id；
                $.post("/demo/news/save",{title:title,content:content,photo:photo,description:description,type:type,newsId:newsId},function(result){
                    if(result.indexOf("success")>=0){
                        alert("提交成功");
                        document.getElementById("newsId").value = result.split(":")[1];
                        $("#myModal").modal('hide');
                        displayMyNews();
                    }
                    else{
                        alert(result);
                    }
                });
            }
            function displayforPhone(){
                var title = document.getElementById("title").value;   //标题
                var content = $('#nanSummernote').summernote('code');   //内容
                var photo = document.getElementById("fileUploadUrl").value;  //缩略图链接
                var description = document.getElementById("comment").value;  //描述
                var type = document.getElementById("alldisplayLabel").value;  //所有标签
                var newsId = document.getElementById("newsId").value; //id
                if(title.length<1){
                    alert("请给你的表演给个标题");
                    return;
                }
                document.getElementById("loading8").style.display = "block";
                $.post("/demo/news/displayforPhone",{title:title,content:content,photo:photo,description:description,type:type,newsId:newsId},function(result){
                    if(result.indexOf("|")>=0){   //说明正确提交
                        $('#phoneDisplayNews').modal({    //弹出框
                        });
                        var imgUrl= result.split("|")[0];   //二维码路径
                        document.getElementById("newsId").value = result.split("|")[1];   //赋值给Id;
                        document.getElementById("phoneNews").innerHTML = "<img src = "+imgUrl +"/ >";
                        document.getElementById("loading8").style.display = "none";
                    }
                    else{
                        document.getElementById("loading8").style.display = "none";
                        $('#myModal').modal({    //弹出框
                        });
                        setTimeout("alert('"+result+"')",500);

                    }
                });
            }
                function uploadFile(obj) {
                    document.getElementById("loading").style.display="block";
                    var formData = new FormData($('#uploadForm')[0]);
                    $.ajax({
                        url: '${projectUrl}/testuploadimg2' ,
                        type: 'POST',
                        data: formData,
                        cache: false,
                        contentType: false,
                        processData: false,
                        success: function (data) {
                            document.getElementById("loading").style.display="none";
                            if("uploadFail"==data){
                                alert("插入失败");
                                return;
                            }
                           document.getElementById("fileUploadUrl").value=data;
                        },
                    });
                }
        function displayMyNews(){    //请求自己的文章
            $('#displayMyNews').modal({  //打开模态框
                keyboard: true
            });
            $.post("/demo/mynews",{},function(result){
                var allcontent = ""
               for(var i = 0;i<result.length;i++){
                  var content = '<a href="${projectUrl}/index?newsId='+result[i].id+'" class="list-group-item">'+
                          '<div style="position:absolute;right:0px" >'+
                          '		<img src="'+result[i].photo+'"  width="50px" height="50px" />'+
                          '		</div>'+
                            '		<h4 class="list-group-item-heading">'+
                            '标题:'+result[i].title+
                            '		</h4>'+
                            '		<p class="list-group-item-text">'+
                            '&nbsp;描述:'+result[i].description+
                            '		</p>'+
                            '	</a>';
                   allcontent =  allcontent+ content;
               }
                document.getElementById("displayallmyNews").innerHTML = allcontent;
                document.getElementById("loading4").style.display="none";
            });
        }
    </script>
</head>

<body>
<div class="alert alert-warning" style="text-align: center">
    <a href="#" class="close" data-dismiss="alert">
        &times;
    </a>
    <strong>Tips:</strong>标签类型现在为试用阶段
</div>
<br>
<div class="input-group input-group-lg" style="margin-left: 30px;margin-right: 25px">
    <span class="input-group-addon">文章标题</span>
    <input type="text" class="form-control" placeholder="请开始你的表演" id="title">
</div>
<div class="m">
    <div class="summernote" id="nanSummernote"></div>
    <div style="text-align: right">
    <input hidden id="newsId" value="0">
     <button type="button" class="btn btn-default btn-lg" onclick="displayforPhone();">手机预览</button>
    <a href="${projectUrl}/index"  target="_Blank"><button type="button" class="btn btn-default btn-lg">新建表演</button></a>
    <button type="button" class="btn btn-default btn-lg" onclick="displayMyNews();">已发表的表演</button>
    <button type="button" class="btn btn-default btn-lg" onclick="saveNews();">点击保存表演</button>
    </div>
</div>
<div class="n">
    <div class="document page" contenteditable="false" id="displayWord">
    </div>
    <div style="float: right">
    <a href="${projectUrl}/images/userManager.png" target="_blank"><button type="button" class="btn btn-default btn-lg" onclick="managerDisplay();">用户管理</button></a>
    <a href="${projectUrl}/images/manager.png" target="_blank"><button type="button" class="btn btn-default btn-lg" onclick="superManagerDisplay();">文章审核</button></a>
    </div>
</div>
<div id="loading5" class="loading"  style="display: none">正在上传图片,请稍后...</div>
<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    请完成以下输入框完成你的表演
                </h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <div id="loading" class="loading"  style="display: none">正在上传...请稍后</div>
                    <label for="photo">缩略图(小于5M):</label>
                    <form id= "uploadForm" enctype="multipart/form-data">
                    <input type="file" class="form-control" name="file" onchange ="uploadFile();" id="fileUpload"/>
                    </form>
                    <div hidden id="displayFileUrl">
                        <span style="font-size: 10px">Tips:下面的链接是缩略图链接，由于文件框无法赋值。无需修改供参考。不影响操作</span><br>
                    <input type="text" hidden id="fileUploadUrl" style="width: 100%;" readonly="true"/>
                    </div>
                </div>
                <div id="displayLabel"></div>
                <input type="text" id="alldisplayLabel" hidden/>
                <div class="form-group">
                    <label for="pwd">标签名称:</label>
                    <input type="text" class="form-control" placeholder="请输入标签,以分号逗号隔开" id="newsLabel">
                </div>
                <div class="form-group">
                    <label for="comment">描述:</label>
                    <textarea class="form-control" rows="5" id="comment"></textarea>
                </div>
             <#--//以下是标签化-->
                <script src="/demo/js/controllerLabel.js"></script>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">关闭
                </button>
                <button type="button" class="btn btn-primary" onclick="submitNews();">
                    提交表演
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<!-- 扫码登陆模态框（Modal） -->
<div class="modal fade" id="scanlogin" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    请打开微信【扫一扫】扫码登陆
                </h4>
            </div>
            <div class="modal-body">
                楠尼玛公告：为了直接使您方便发表,我们强制您必须先登录才方可开始您的表演<br>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如果二维码未出现，窗口不关闭，则可能您的浏览不支持或者重新刷新页面
                <div id="loading2" class="loading"  style="display: block">正在建立连接，请稍后...</div>
                <div id="erweimaDisplay" style="text-align: center"></div>

               </div>
              <div id="loading3" class="loading"  style="display: none">被扫描成功，等待确认登陆，请稍后...</div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" title="温馨提示"
                        data-container="body" data-toggle="popover" data-placement="top"
                        data-content="都说好强制了怎么可能让你关闭">
                    强制关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<!-- 扫码手机预览模态框（Modal） -->
<div class="modal fade" id="phoneDisplayNews" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    请打开微信【扫一扫】扫码预览
                </h4>
            </div>
            <div class="modal-body">
                楠尼玛:请打开微信【扫一扫】扫码预览
               <br>
                <div id="phoneNews" style="text-align: center"></div>

            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">
                    关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<div id="loading8" class="loading"  style="display: none">正在加载...请稍后...</div>
    <!-- 我的文章模态框（Modal） -->
    <div class="modal fade" id="displayMyNews" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
         data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    </button>
                    <h4 class="modal-title" id="myModalLabel">

                    </h4>
                </div>
                <div class="modal-body">
                    <div style="text-align: center">
                    <strong>楠尼玛公告：为了不给您带来不必要的麻烦。<br>我们强制您必须先登录才方可开始您的表演与其他操作</strong><br><br>
                    </div>
                    <div id="loading4" class="loading"  style="display: block">正在加载您的文章列表,请稍后</div>
                    <div>
                        <div class="list-group">
                            <a class="list-group-item active" style="text-align: center" >
                                <h4 class="list-group-item-heading">
                                    以下是已发表的文章
                                </h4>
                            </a>
                            <div id="displayallmyNews" style="overflow:auto;height:300px">


                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">
                         关闭
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>
    <script>
        $(function () { $('#scanlogin').modal({
            keyboard: false
        })});
        $(function () {
            $("[data-toggle='popover']").popover();
        });
    </script>
<script>
    var websocket = null;
    if('WebSocket' in window){
        websocket = new WebSocket('ws://www.linge.wang/demo/websocket');
//        websocket = new WebSocket('ws://www.linge.wang/demo/websocket');
    }
    else{
        document.getElementById("loading2").style.display="none";
        alert("浏览器不支持,请换一个浏览器");
    }
    websocket.onopen = function(event){
        console.log("建立连接");
        $.get("${projectUrl}/getCodeImgUrl", function(result){
            if(result=="logined"){
                document.getElementById("loading2").style.display="none";
            }
            else {
                document.getElementById("loading2").style.display = "none";
                document.getElementById("erweimaDisplay").innerHTML = "<img src='" + result + "'>";
            }
        });

    }
    websocket.onclose = function(event){
        console.log("连接关闭")
    }
    websocket.onmessage = function (event){
        if(event.data=="scansuccess"){    //成功被扫码
            document.getElementById("loading3").style.display="block";
        }
        if(event.data.indexOf("successLogin")>=0){     //成功授权登陆
            var openid = event.data.split(":")[1];
            $.post("${projectUrl}/setSessionId",{openid:openid}, function(result){
                if(result=="success"){
                    console.log("登陆成功");
                    document.getElementById("loading3").style.display="none";
                    $('.btn-primary').popover('hide');
                    $('#scanlogin').modal('hide');
                }
                else{
                    alert("授权失败,请刷新页面重试授权");
                }
        });
    }
    if(event.data=="fail"){
        document.getElementById("loading3").style.display="none";
        alert("授权失败,请刷新页面重试授权");
    }
    }
    websocket.onerror = function(event){
        document.getElementById("loading2").style.display="none";
        alert("通信发成错误,请重新刷新页面");
    }
    window.onbeforeunload = function(){
        websocket.close();
    }
    window.onload = function(){
        var status = '${scanuserid!""}';
        if(status!="undefind"){   //则代表已经登陆过了
            console.log("登陆状态:已登陆");
            $('.btn-primary').popover('hide');
            $('#scanlogin').modal('hide');
        }
        $('.note-editable')[0].onkeyup = function(){
            displayNews();
        };
        window.setInterval("displayNews()",200);

        var information = '${information!""}';
        var title =  '${information.title!""}';
        var content = '${information.content!""}';
        var photo =  '${information.photo!""}';
        var labels = '${information.type!""}';
        var description = '${information.description!""}';
        var newsId = '${information.id!""}';
        if(title.length>0){
            document.getElementById("title").value = title;
        }
        if(content.length>0){
            $('#nanSummernote').summernote('code',content);
        }
        if(photo.length>0){
            document.getElementById("displayFileUrl").style.display="block";
            document.getElementById("fileUploadUrl").style.display="block";
            document.getElementById("fileUploadUrl").value = photo;
        }if(labels.length>0){
           var labels =  labels.split(",");
           var allLabels = "";
           for(var i = 0 ; i<labels.length-1;i++){
               allLabels = allLabels+ '<span class="badge badge-secondary displayLabel" style="font-size:20px" id=\'label_'+count+'\' onclick="deleteLabel(\''+count+'\')">'
                       +labels[i]+'</span>   ';
               count = count+1;
           }
            var changedisplayLabel = document.getElementById("displayLabel");
            changedisplayLabel.innerHTML = allLabels;  //展示区
            document.getElementById("alldisplayLabel").value=labels;  //真正值
        }if(description.length>0){
            document.getElementById("comment").value = description;  //描述
        }if(newsId.length>0){
              document.getElementById("newsId").value = newsId;
        }
    }
</script>
</body>

</html>
