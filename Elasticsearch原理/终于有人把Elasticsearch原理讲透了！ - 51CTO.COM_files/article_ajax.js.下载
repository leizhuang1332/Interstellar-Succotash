// 推荐职位
// $(function(){
//     var url = window.location.host;
//     var type = url.split('.');
//     $.ajax({
//         url: 'https://www.51cto.com/php/getRecommendJob.php',
//         type: 'get',
//         data: {category:type[0]},
//         dataType: 'jsonp',
//         jsonp: 'callback',
//         success: function(result){
//             if(result.error_code == 0){
//                 var str = '';
//                 var data = result.data;
//                 for (var i in data) {
//                     if (data[i].salary) {
//                         var salary = data[i].salary;
//                     } else {
//                         var salary = data[i].salary_min +'-'+data[i].salary_max;
//                     }
//                     str += '<dl><dt><a href="'+data[i].url+'" target="_blank" > '+data[i].position+' </a></dt>';
//                     str += '<dd><h3>' + data[i].job_nature + '/' + data[i].work_year + '/' + data[i].lowest_degrees + '</h3>';
//                     str += '<span>' + salary + '</span><a href=" '+ data[i].company_url +' " target="_blank">'+ data[i].short_name +'</a></dd></dl>';
//                 }
//                 $("#rmzw").html(str);
//             } else {
//                 $("#rmzw").html('暂无推荐职位');
//             }
//         },
//         error: function(result){
//             $("#rmzw").html('暂无推荐职位');
//         }
//     })
// });
// 精彩评论
$(function(){
    $.ajax({
        url: 'https://comment.51cto.com/index.php?do=index&m=splendidCmt',
        type: 'get',
        data: {num: 4},
        dataType: 'jsonp',
        jsonp: 'callback',
        success: function(result){
            if (result.status == 1) {
                var str = '';
                var data = result.data;
                str += '<div class="jcpl m30"><h2><span>精彩评论</span></h2>'
                for (var i in data) {
                    str += '<dl><dt><a href="'+data[i].home_url+'"><img src="'+data[i].avator+'" width="45px" height="45px"></a></dt>';
                    str += '<dd><a href="'+data[i].home_url+'"><h3>'+data[i].user_name+'</a>评论了：<a href="'+data[i].artcle_url+'">'+data[i].artcle_title+'</a></h3>';
                    str += '<h4>'+data[i].comment_content+'</h4></dd></dl>';
                }
                str += '</div>';
                $('#jcpl').html(str);
            } else {
                return ;
            }
        },
        error: function(result){
            return ;
        }
    })
});
$(function(){
    /**
     * 大家都在看
     */
    $.ajax({
        url: 'https://www.51cto.com/php/article_ajax.php',
        type: 'get',
        data: {type: 'ourfavorite', row: 4},
        dataType: 'jsonp',
        jsonp: 'callback',
        success: function(result){
            var str = '';
            var data = result.data;
            for (var i in data) {
                if (i == 3) {
                    str += '<div><a href="'+data[i].url+'"><img src="'+data[i].picname+'" title="'+data[i].title+'"></a><a href="'+data[i].url+'" title="'+data[i].title+'">'+data[i].title+'</a></div>';
                } else {
                    str += '<div class="m15"><a href="'+data[i].url+'"><img src="'+data[i].picname+'" title="'+data[i].title+'"></a><a href="'+data[i].url+'" title="'+data[i].title+'">'+data[i].title+'</a></div>';
                }
            }
            $("#djdzk").html(str);
        },
        error: function(result){
            return '';
        }
    });
    /**
     * 猜你喜欢
     */
    $.ajax({
        url: 'https://www.51cto.com/php/article_ajax.php',
        type: 'get',
        data: {type: 'yourfavorite', row: 4, id: artid},
        dataType: 'jsonp',
        jsonp: 'callback',
        success: function(result){
            var str = '';
            var data = result.data;
            for (var i in data) {
                if (i == 3) {
                    str += '<div><a href="'+data[i].url+'"><img src="'+data[i].thumb+'" title="'+data[i].title+'"></a><a href="'+data[i].url+'" title="'+data[i].title+'">'+data[i].title+'</a></div>';
                } else {
                    str += '<div class="m15"><a href="'+data[i].url+'"><img src="'+data[i].thumb+'" title="'+data[i].title+'"></a><a href="'+data[i].url+'" title="'+data[i].title+'">'+data[i].title+'</a></div>';
                }
            }
            $("#cnxh").html(str);
        },
        error: function(result){
            return '';
        }
    })
});

$(function(){
    $.ajax({
        url: 'https://www.51cto.com/php/article_ajax.php',
        type: 'get',
        data: {type: 'praisecounts', id: artid},
        dataType: 'jsonp',
        jsonp: 'callback',
        success: function(result){
            var num = result.num;
            $('.zhan span').html(num);
        }
    });
    var praise = getCookie('praise');
    if (praise){
        if (praise.indexOf(artid) >= 0) {
            $(".dzdz").css("background","url(https://static4.51cto.com/51cto/cms/2016/images/dzdz.jpg) no-repeat 8px -70px");
        }
    }
});

$('a.zhan').click(function() {

    $.get('https://www.51cto.com/php/article_ajax.php',{type: 'praise', id: artid},function(result){
        if (result.code == 1) {
            var left = parseInt($('a.zhan').offset().left) + 10,
                top = parseInt($('a.zhan').offset().top) - 10,
                obj = $('a.zhan');
            $('a.zhan').append('<div class="zhans"><b>+1<\/b><\/div>');
            $('.zhans').css({
                'position': 'absolute',
                'z-index': '1',
                'color': '#C30',
                'left': left + 'px',
                'top': top + 'px'
            }).animate({
                top: top - 10,
                left: left + 10
            }, 'slow', function () {
                $(this).fadeIn('fast').remove();
                var Num = parseInt(obj.find('span').text());
                Num++;
                obj.find('span').text(Num);

            });
            $(".dzdz").css("background","url(https://static4.51cto.com/51cto/cms/2016/images/dzdz.jpg) no-repeat 8px -70px");
            return false;
        } else if (result.code == -1) {
            alert('您已经赞过了~'); return false;
        } else {
            alert('点赞失败'); return false;
        }
    },'jsonp')
});
function getCookie(name)
{
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}