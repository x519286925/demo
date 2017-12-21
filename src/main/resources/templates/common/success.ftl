<html>
<head>
    <meta charset="utf-8">
    <title>${msg!""}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/meetingSign/css/pintuer.css">
    <script src="/meetingSign/js/jquery.js"></script>
    <script src="/meetingSign/js/pintuer.js"></script>
</head>
<body>
<div class="keypoint bg-blue bg-inverse radius text-center">
    <h1>操作成功！</h1>
    <p>
        <strong>${msg!""}</strong>  <a href="${url!""}" class="alert-link">3秒自动跳转</a></p>
</div>



</body>
<script>
    setTimeout('location.href="${url}"',3000);

</script>
</html>
