
// tedu-N7 [rum3zGHI9Y]
(function() {
  $(".tedu-N7").each(function() {
    const $block = $(this);
    $(function() {
      var visualSwiper = new Swiper(".tedu-N7 .visual-swiper", {
        speed: 600,
        parallax: true,
        loop: true,
        touchEventsTarget: "wrapper",
        slidesPerView: "auto",
        autoplay: {
          delay: 2500,
          disableOnInteraction: false,
        },
        pagination: {
          el: ".tedu-N7 .visual-swiper .control-wrap .swiper-pagination",
          clickable: "true",
        },
        on: {
          slideChange: function() {
            var activeSlide = this.slides[this.activeIndex];
            if ($(activeSlide).hasClass('btype')) {
              $(this.$el).addClass('btype-swiper');
            } else {
              $(this.$el).removeClass('btype-swiper');
            }
          }
        },
      });
      // Swiper Play, Pause Button
      const pauseButton = $block.find(".swiper-button-pause");
      const playButton = $block.find(".swiper-button-play");
      playButton.hide();
      pauseButton.show();
      pauseButton.on("click", function() {
        visualSwiper.autoplay.stop();
        playButton.show();
        pauseButton.hide();
      });
      playButton.on("click", function() {
        visualSwiper.autoplay.start();
        playButton.hide();
        pauseButton.show();
      });
    });
    const $header = $(".header-container");
    // Background Color
    if ($header.hasClass("darkmode")) {
      $block.addClass("darkmode");
      $block.removeClass("lightmode");
    } else if ($header.hasClass("lightmode")) {
      $block.addClass("lightmode");
      $block.removeClass("darkmode");
    }
  });
})();
// tedu-N8 [WKM3ZghIbZ]
(function() {
  $(".tedu-N8").each(function() {
    const $block = $(this);
    $(function() {
      var contSwiper = new Swiper(".tedu-N8 .content-swiper", {
        slidesPerView: 1,
        spaceBetween: 20,
        loop: false,
        speed: 500,
        navigation: {
          nextEl: ".content-swiper .swiper-button-next",
          prevEl: ".content-swiper .swiper-button-prev",
        },
        breakpoints: {
          768: {
            slidesPerView: 2,
            spaceBetween: 20,
          },
          1024: {
            slidesPerView: 3,
            spaceBetween: 40,
          },
        },
      });
      //Bookmark
      $block.find(".ico-bookmark").on("click", function() {
        const $this = $(this);
        $this.parents(".card-wrap").toggleClass("badge");
      });
	  
      const $header = $(".header-container");
      // Background Color
      if ($header.hasClass("darkmode")) {
        $block.addClass("darkmode");
        $block.removeClass("lightmode");
      } else if ($header.hasClass("lightmode")) {
        $block.addClass("lightmode");
        $block.removeClass("darkmode");
      }
    });
  });
})();
// tedu-N10 [qYM3ZghIHt]
(function() {
  $(function() {
    $(".tedu-N10").each(function() {
      const $block = $(this);
      const $header = $(".header-container");
      // Background Color
      if ($header.hasClass("darkmode")) {
        $block.addClass("darkmode");
        $block.removeClass("lightmode");
      } else if ($header.hasClass("lightmode")) {
        $block.addClass("lightmode");
        $block.removeClass("darkmode");
      }
    });
  });
})();
// tedu-N12 [eiM3ZGHiL1]
(function() {
  $(function() {
    $(".tedu-N12").each(function() {
      const $block = $(this);
      const $header = $(".header-container");
      // Background Color
      if ($header.hasClass("darkmode")) {
        $block.addClass("darkmode");
        $block.removeClass("lightmode");
      } else if ($header.hasClass("lightmode")) {
        $block.addClass("lightmode");
        $block.removeClass("darkmode");
      }
    });
  });
})();
// tedu-N15 [KaM3zgHIO7]
(function() {
  $(".tedu-N15").each(function() {
    const $block = $(this);
    $(function() {
      var contSwiper = new Swiper(".tedu-N15 .content-swiper", {
        slidesPerView: 1,
        spaceBetween: 20,
        loop: false,
        speed: 500,
        navigation: {
          nextEl: ".content-swiper .swiper-button-next",
          prevEl: ".content-swiper .swiper-button-prev",
        },
        breakpoints: {
          768: {
            slidesPerView: 2,
            spaceBetween: 20,
          },
          1024: {
            slidesPerView: 3,
            spaceBetween: 40,
          },
        },
      });
    });
  });
})();
// tedu-N53 [JEm3ZGoaP4]
(function() {
  $(function() {
    $(".tedu-N53").each(function() {
      const $block = $(this);
      const $header = $(".header-container");
      const $headerHeight = $header.height();
      // HeaderHeight
      $(document).ready(function() {
        $block.css({
          "top": $headerHeight + 50 + "px"
        })
      })
      // Scroll
      $(window).on("load scroll", function() {
        const $thisTop = $(this).scrollTop();
        if ($thisTop > 0) {
          $block.addClass("sticky");
          var fromBottom = $(document).height() - $thisTop - $(window).height();
          var maxDistance = 200;
          if (fromBottom > maxDistance) {
            $block.css('bottom', '400px');
          }
          if (window.innerWidth <= 980) {
            $block.removeClass("sticky");
            $block.addClass("default");
          }
        } else {
          $block.removeClass("sticky");
          $block.css('bottom', 'auto');
        }
      });
      // Lecture
      $block.find(".btn-lecture").on("click", function() {
        $(this).hide();
        $block.find(".sidebar").addClass("show");
      });
      $block.find(".btn-lecture-close").on("click", function() {
        $block.find(".btn-lecture").show();
        $block.find(".sidebar").removeClass("show");
      });
      // Mobile Area
      $(window).on('resize', function() {
        if (window.innerWidth >= 980) {
          $block.removeClass("default");
          $block.find(".sidebar").removeClass("show");
        } else if (window.innerWidth <= 980) {
          $block.removeClass("sticky");
          $block.addClass("default");
        }
      });
    });
  });
})();
// tedu-N54 [IPm3zgOaT0]
(function() {
  $(function() {
    $(".tedu-N54").each(function() {
      const $block = $(this);
      // Tab Menu 
      $block.find(".tab-menu").on("click", function() {
        const $this = $(this);
        $this.addClass("active");
        $this.siblings().removeClass("active");
      })
    });
  });
})();
// tedu-N18 [bGm3ZGomq3]
(function() {
  $(function() {
    $(".tedu-N18").each(function() {
      const $block = $(this);
      const $header = $(".header-container");
      // Background Color
      if ($header.hasClass("darkmode")) {
        $block.addClass("darkmode");
        $block.removeClass("lightmode");
      } else if ($header.hasClass("lightmode")) {
        $block.addClass("lightmode");
        $block.removeClass("darkmode");
      }
    });
  });
})();
// tedu-N20 [euM3zGon0M]
(function() {
  $(function() {
    $(".tedu-N20").each(function() {
      const $block = $(this);
      const $header = $(".header-container");
      const $headerHeight = $header.height();
      // HeaderHeight
      $(document).ready(function() {
        $block.css({
          "top": $headerHeight + 50 + "px"
        })
      })
      //Bookmark
      $block.find(".ico-bookmark").on("click", function() {
        const $this = $(this);
        $this.parents(".sidebar").toggleClass("badge");
      });
	  /*// ico-heart 클릭 이벤트 추가
	        $block.find(".ico-heart").on("click", function () {
	          const $this = $(this);
	          const $sidebar = $this.parents(".sidebar");
	          $sidebar.toggleClass("badge"); // badge 클래스 토글
	        });*/
      // Scroll
      $(window).on("load scroll", function() {
        const $thisTop = $(this).scrollTop();
        if ($thisTop > 0) {
          $block.addClass("sticky");
          var fromBottom = $(document).height() - $thisTop - $(window).height();
          var maxDistance = 200;
          if (fromBottom > maxDistance) {
            $block.css('bottom', '280px');
          }
          if (window.innerWidth <= 980) {
            $block.removeClass("sticky");
            $block.addClass("default");
          }
        } else {
          $block.removeClass("sticky");
          $block.css('bottom', 'auto');
        }
      });
      // Mobile Area
      $(window).on('resize', function() {
        if (window.innerWidth >= 980) {
          $block.removeClass("default");
        } else if (window.innerWidth <= 980) {
          $block.removeClass("sticky");
          $block.addClass("default");
        }
      });
    });
  });
})();
// tedu-N22 [mlM3zGOn6A]
(function() {
  $(function() {
    $(".tedu-N22").each(function() {
      const $block = $(this);
      // Tab Menu 
      $block.find(".tab-menu").on("click", function() {
        const $this = $(this);
        $this.addClass("active");
        $this.siblings().removeClass("active");
      })
    });
  });
})();
// tedu-N23 [jTM3ZgoNaO]
(function() {
  $(function() {
    $(".tedu-N23").each(function() {
      const $block = $(this);
      const $header = $(".header-container");
      // Background Color
      if ($header.hasClass("darkmode")) {
        $block.addClass("darkmode");
        $block.removeClass("lightmode");
      } else if ($header.hasClass("lightmode")) {
        $block.addClass("lightmode");
        $block.removeClass("darkmode");
      }
    });
  });
})();
// tedu-N27 [iKm3zGOnl6]
(function() {
  $(".tedu-N27").each(function() {
    const $block = $(this);
    $(function() {
      var contSwiper = new Swiper(".tedu-N27 .review-swiper", {
        slidesPerView: 1,
        spaceBetween: 12,
        loop: false,
        centeredSlides: false,
        speed: 500,
        navigation: {
          nextEl: ".review-swiper .swiper-button-next",
          prevEl: ".review-swiper .swiper-button-prev",
        },
        breakpoints: {
          768: {
            slidesPerView: 1.5,
            spaceBetween: 12,
          },
          980: {
            slidesPerView: 1,
            spaceBetween: 12,
          },
          996: {
            slidesPerView: 1,
            spaceBetween: 12,
          },
          1024: {
            slidesPerView: 1,
            spaceBetween: 20,
          },
        },
      });
    });
  });
})();
// tedu-N29 [Oxm3zGonog]
(function() {
  $(function() {
    $(".tedu-N29").each(function() {
      const $block = $(this);
      // accordion
      $block.find(".acc-btn").on("click", function() {
        const $this = $(this);
        $this.parents(".acc-item").addClass("active");
        $this.parents(".acc-item").siblings().removeClass("active");
      })
    });
  });
})();
// tedu-N31 [Iym3ZGoO3x]
(function() {
	$(function () {
	    $(".tedu-N31").each(function () {
	      const $block = $(this);
	      /*// ico-heart 클릭 이벤트 추가
	      $block.find(".ico-heart").on("click", function () {
	        const $this = $(this);
	        $this.parents(".card-wrap").toggleClass("badge");
	      });*/
		  $block.find(".ico-bookmark").on("click", function () {
		  	        const $this = $(this);
		  	        $this.parents(".card-wrap").toggleClass("badge");
		  	      });
    });
  });
})();
// tedu-N45 [xJm3zgpm8A]
(function() {
  $(function() {
    $(".tedu-N45").each(function() {
      const $block = $(this);
      const $header = $(".header-container");
      const $headerHeight = $header.height();
      // HeaderHeight
      $(document).ready(function() {
        $block.css({
          "top": $headerHeight + 130 + "px"
        })
      })
      // Scroll
      $(window).on("load scroll", function() {
        const $thisTop = $(this).scrollTop();
        if ($thisTop > 0) {
          $block.addClass("sticky");
          var fromBottom = $(document).height() - $thisTop - $(window).height();
          var maxDistance = 200;
          if (fromBottom > maxDistance) {
            $block.css('bottom', '520px');
          }
          if (window.innerWidth <= 980) {
            $block.removeClass("sticky");
            $block.addClass("default");
          }
        } else {
          $block.removeClass("sticky");
          $block.css('bottom', 'auto');
        }
      });
      // Mobile Area
      $(window).on('resize', function() {
        if (window.innerWidth >= 980) {
          $block.removeClass("default");
        } else if (window.innerWidth <= 980) {
          $block.removeClass("sticky");
          $block.addClass("default");
        }
      });
    });
  });
})();
// tedu-N47 [gKM3zgP8b1]
(function() {
  $(function() {
    $(".tedu-N47").each(function() {
      const $block = $(this);
      // Tab Menu 
      $block.find(".tab-menu").on("click", function() {
        const $this = $(this);
        $this.addClass("active");
        $this.siblings().removeClass("active");
      })
    });
  });
})();