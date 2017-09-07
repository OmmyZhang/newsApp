# 说明

+ 新闻素材的获取

  *GetNews类* (org.attentiveness.news.net.GetNews)

  提供接口

  + GetNews(HashSet<String> classTags , HashSet<String> notShowWords , int pageSize);
  + ArrayList<HashMap> getMore()
  + void search(String keyword)
  + ArrayList<HashMap> search_more()

  第一次进入和刷新请新构造GetNews对象，上拉获取更多请使用getMore()。

  对于getMore()和search_more()，当没有更多时返回null，当有但是全部不符合分类或者被屏蔽时返回长度0的ArrayList，请适当处理。

  注意图片链接可能有多于一张，请使用正则表达式解析。