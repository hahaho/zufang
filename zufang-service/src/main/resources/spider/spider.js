var page = require('webpage').create(),
    system = require('system'),
    address,host,referer;


if (system.args.length === 1) {
    console.log('phantomjs exit....');
//这行代码很重要。凡是结束必须调用。否则phantomjs不会停止
    phantom.exit();
}
page.settings.resourceTimeout = 20000;//超过20秒放弃加载

address = system.args[1];
host = system.args[2];
referer = system.args[3];


//设置header
var settings = {
    encoding:"utf8",
    headers:{
        "Accept-Language":"zh-CN,zh;q=0.9",
        "Cache-Control":"max-age=0",
        "Host":host,
        "Proxy-Connection":"keep-alive",
        "Referer":referer,
        "Upgrade-Insecure-Requests":"1",
        "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36"
    },
};
page.onConsoleMessage = function(msg) {
    console.log(msg);
};

//css 文件不加载
page.onResourceRequested = function(requestData, request) {
    if ((/http:\/\/.+?\.css$/gi).test(requestData['url'])) {
        console.log('Skipping', requestData['url']);
        request.abort();
    }
};

page.open(address, settings,function (status) {
    console.log('----system args:' + system.args)
    console.log('---------Status: ' + status);
    if (status !== 'success') {

        console.log('Unable to request url: ' + address);
    } else {

            phantom.outputEncoding = 'utf8';

            console.log('--------page html: ' + page.content);//最后返回webkit加载之后的页面内容

    }
    phantom.exit();
});
