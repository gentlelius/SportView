package ls.main.bean;


import java.util.List;

public class NewsList {
    /**
     * postid : PHOT4EDK000554TR
     * hasCover : true
     * hasHead : 1
     * replyCount : 2
     * hasImg : 1
     * digest : 
     * hasIcon : true
     * docid : 9IG74V5H00963VRO_BTCG73UFpeihanupdateDoc
     * title : 康保草原马拉松：连天草场迷醉跑者
     * order : 1
     * priority : 75
     * lmodify : 2016-08-01 09:37:32
     * boardid : sports2_bbs
     * wap_portal : [{"title":"训练指南","subtitle":"Guide","imgsrc":"http://img2.cache.netease.com/m/newsapp/Guide.png","url":"http://running.sports.163.com/runningguide"},{"title":"赛事报名","subtitle":"Register","imgsrc":"http://img2.cache.netease.com/m/newsapp/Register.png","url":"http://running.sports.163.com/register/index_mobile.html"}]
     * photosetID : 54TR0005|145844
     * template : normal1
     * votecount : 0
     * skipID : 54TR0005|145844
     * alias : Running
     * skipType : photoset
     * cid : C1348649048655
     * hasAD : 1
     * source : 网易原创
     * imgsrc : http://cms-bucket.nosdn.127.net/e57d83ea137f440dbde0611f2d035fcb20160801093726.jpeg
     * tname : 跑步
     * ename : paobu
     * ptime : 2016-08-01 09:34:58
     */

    private List<T1411113472760Bean> T1411113472760;

    public List<T1411113472760Bean> getT1411113472760() {
        return T1411113472760;
    }

    public void setT1411113472760(List<T1411113472760Bean> T1411113472760) {
        this.T1411113472760 = T1411113472760;
    }

    public static class T1411113472760Bean {
        private boolean hasCover;
        private int replyCount;
        private String title;
        private int order;
        private String source;
        private String imgsrc;
        private String ptime;
        private String url;
        
        
        public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		/**
         * title : 训练指南
         * subtitle : Guide
         * imgsrc : http://img2.cache.netease.com/m/newsapp/Guide.png
         * url : http://running.sports.163.com/runningguide
         */

        private List<WapPortalBean> wap_portal;

        public boolean isHasCover() {
            return hasCover;
        }

        public void setHasCover(boolean hasCover) {
            this.hasCover = hasCover;
        }

        public int getReplyCount() {
            return replyCount;
        }

        public void setReplyCount(int replyCount) {
            this.replyCount = replyCount;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getImgsrc() {
            return imgsrc;
        }

        public void setImgsrc(String imgsrc) {
            this.imgsrc = imgsrc;
        }

        public String getPtime() {
            return ptime;
        }

        public void setPtime(String ptime) {
            this.ptime = ptime;
        }

        public List<WapPortalBean> getWap_portal() {
            return wap_portal;
        }

        public void setWap_portal(List<WapPortalBean> wap_portal) {
            this.wap_portal = wap_portal;
        }

        public static class WapPortalBean {
            private String title;
            private String subtitle;
            private String imgsrc;
            private String url;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }

            public String getImgsrc() {
                return imgsrc;
            }

            public void setImgsrc(String imgsrc) {
                this.imgsrc = imgsrc;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}