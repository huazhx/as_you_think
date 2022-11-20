from time import sleep
import requests
from lxml import etree
import random
import re
import os
import pymysql

class BbsSpider():
    def __init__(self) -> None:
        self.one_url='https://bbs.byr.cn/board/focus?p={}&_uid=wxk123'
        self.two_url='https://bbs.byr.cn/{}'  # 网站主地址
        # 以后我们就用 session 来访问 北邮人论坛
        self.session=requests.session()
        form_data={
            'id':'',
            'passwd':'',
            # 'mode':0,
            # 'CookieDate':1,
        }
        self.session.post(url="https://bbs.byr.cn/user/ajax_login.json",       
                headers=self.getHeader(),
                data=form_data)

        # 创建2个对	象
        self.db = pymysql.connect('59.110.112.117', 'root', 'Hadoop@1234', 'index_result', charset='utf8')
        self.cursor = self.db.cursor()

    def getHeader(self):
        '''返回我们需要的请求头'''
        headers={'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.114 Safari/537.36 Edg/103.0.1264.62',
            # 下列是经过测试不需要发送的 信息
            #'cookie':'left-index=0000000000; Hm_lvt_38b0e830a659ea9a05888b924f641842=1668220950,1668223187; login-user=wxk123; nforum[UTMPUSERID]=guest; nforum[UTMPKEY]=69196509; nforum[UTMPNUM]=3301; Hm_lpvt_38b0e830a659ea9a05888b924f641842=1668255354',
            #'content-length':'29',
            #'origin':'https://bbs.byr.cn',
            #'referer':'https://bbs.byr.cn/',
            #'sec-fetch-site':'same-origin',
            'x-requested-with':'XMLHttpRequest'  #必不可少
            }
        return headers
    def getHtml(self,url):
        '''返回的是访问网址的字符串内容'''
        # print('----')
        res=self.session.get(url=url,
                headers=self.getHeader()  
                ).content.decode('GBK','ignore')
        # print(res)
        return res
    def xpathParse(self,html,xpath_bds):
        '''xpath解析文章内容'''
        p = etree.HTML(html)
        r_list = p.xpath(xpath_bds)
        # print(r_list)
        return r_list
    def reParse(self,html,re_bds):
        '''re解析文章内容'''
        pattern=re.compile(re_bds,re.S)
        r_list=pattern.findall(html)
        return r_list
    def parseHtml(self,one_url):
        '''真正调度的函数'''
        one_html = self.getHtml(one_url)
        # xpath_bds='//*[@id="body"]/text()'
        # tbody=self.xpathParse(one_html,xpath_bds)
        re_bds=r'<tr ><td class="title_8.*?><a.*?href="(.*?)"'
        article_link=self.reParse(one_html,re_bds)

        # print(len(article_link))
        for link in article_link:
            item={}
            item['link']=self.two_url.format(link)
            two_html=self.getHtml(item['link'])
            # article 文章内容
            re_bds=r'站内(.*?)※'
            try:
                item['article']=self.reParse(two_html,re_bds)[0].replace('&nbsp;','').replace('<br />','\n')
            except:
                continue
            xiaochu=re.compile(r'<.*?>')
            item['article']=xiaochu.sub('',item['article'])
            # article 文章标题
            re_bds=r'文章主题:&ensp;(.*?)</span>'
            try:
                item['title'] =self.reParse(two_html,re_bds)[0]
            except:
                continue
            # article 文章作者
            re_bds=r'发信人: (.*?), 信区'
            try:
                item['author'] =self.reParse(two_html,re_bds)[0]
            except:
                continue
            # article 日期
            re_bds=r'北邮人论坛 \((.*?)\), 站内'
            try:
                item['date'] =self.reParse(two_html,re_bds)[0].replace('&nbsp;',' ')
            except:
                continue
            item['type']='uncertain'
            print(item)
            self.save(item)
            
            # sleep(random.randint(1,2))

    def save(self,item):
        '''标题、文章内容、作者、日期'''
        
        ins = 'insert into essays (title,author,date,type,content)values(%s,%s,%s,%s,%s)'
        try:
            self.cursor.execute(ins,[item['title'],item['author'],item['date'],item['type'],item['article']])
        except:
            self.db.rollback()
            return
        self.db.commit()

    def run(self):
        for i in range(1,46):
            self.parseHtml(self.one_url.format(i))

spider=BbsSpider()
spider.run()
