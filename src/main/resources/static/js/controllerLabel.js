//
function deleteLabel(id){
    var label = document.getElementById("label_"+id);
    label.parentNode.removeChild(label);
    //来获得所有标签
    var allLabel = $(".displayLabel");
    var allLabelContent = "";   //所有标签内容
    for(var i = 0;i<allLabel.length;i++){
        allLabelContent = allLabelContent+allLabel[i].innerHTML+",";
    }
    document.getElementById("alldisplayLabel").value=allLabelContent;
}
// var newsLabel = document.getElementById("newsLabel");  //输入框
// var newslabelTips = document.getElementById("newslabelTips").innerHTML;  //提示语
// var displayLabel = document.getElementById("displayLabel").innerHTML;  //展示区域
// var count = 1;
// newsLabel.onclick = function(){
//     if(newsLabel.innerHTML.indexOf(newslabelTips)>=0){  //包含提示语则清空
//         newsLabel.innerHTML = "";
//     }
// }
// newsLabel.onblur = function(){
//     if(newsLabel.innerHTML.length==0){
//         newsLabel.innerHTML =
//             '<span style="color:#CCC" id="newslabelTips">'+newslabelTips+"</span>";
//     }
// }
var count = 1;
var newsLabel = document.getElementById("newsLabel");  //输入框
newsLabel.onkeyup = function(){
    if(newsLabel.value.indexOf(",")>=0|newsLabel.value.indexOf(";")>=0|newsLabel.value.indexOf("，")>=0|newsLabel.value.indexOf("；")>=0){  //包含逗号
        var splitLabel = newsLabel.value.substr(0,newsLabel.value.length-1);  //去除逗号(即标签内容)
        //展示区
        var changedisplayLabel = document.getElementById("displayLabel");
        changedisplayLabel.innerHTML = changedisplayLabel.innerHTML
            + '<span class="badge badge-secondary displayLabel" style="font-size:20px" id=\'label_'+count+'\' onclick="deleteLabel(\''+count+'\')">'
            +splitLabel+'</span>   ';
        count = count+1;
        newsLabel.value = "";  //清空
        //来获得所有标签
        var allLabel = $(".displayLabel");
        var allLabelContent = "";   //所有标签内容
        for(var i = 0;i<allLabel.length;i++){
            allLabelContent = allLabelContent+allLabel[i].innerHTML+",";
        }
        document.getElementById("alldisplayLabel").value=allLabelContent;
    }
}
