/* tedu_light-N1 [edM5brniiS] */
(function() {
  $(function() {
    $(".tedu_light-N1[id=\'edM5brniiS\']").each(function() {
      const $block = $(this);
      // Header Mobile 1Depth Click
      if (window.innerWidth <= 992) {
        $block.find(".header-gnbitem").each(function() {
          const $gnblink = $(this).find(".header-gnblink");
          const $sublist = $(this).find(".header-sublist");
          if ($sublist.length) {
            $gnblink.attr("href", "javascript:void(0);");
          }
        });
      }
      // Header Top
      const $headerTop = $block.find(".header-top");
      const $gnbList = $block.find(".header-gnbitem");
      if ($headerTop.length && $gnbList.length) {
        $block.addClass("top-menu-active");
      }
      // Mobile Top
      $block.find(".btn-momenu").on("click", function() {
        $block.toggleClass("block-active");
        $block.find(".header-gnbitem").removeClass("item-active");
        $block.find(".header-sublist").removeAttr("style");
      });
      // Mobile Gnb
      function handleGnbEvents() {
        $block.find(".header-gnbitem").each(function() {
          const $this = $(this);
          const $thislink = $this.find(".header-gnblink");
          const $sublist = $this.find(".header-sublist");
          $thislink.off("click");
          if (window.innerWidth < 992) {
            $thislink.on("click", function() {
              const $clickedItem = $(this).parents(".header-gnbitem");
              if (!$clickedItem.hasClass("item-active")) {
                $block.find(".header-gnbitem").removeClass("item-active");
                $block.find(".header-sublist").stop().slideUp(300);
              }
              $clickedItem.toggleClass("item-active");
              $sublist.stop().slideToggle(300);
            });
          } else {
            $sublist.removeAttr("style");
            $block.removeClass("block-active");
            $block.find(".header-gnbitem").removeClass("item-active");
          }
        });
      }
      handleGnbEvents();
      $(window).on("resize", function() {
        handleGnbEvents();
      });
      //Scroll Header
      if ($(window).scrollTop() === 0) {
        $block.addClass("header-top-active");
      }
      $(window).scroll(function() {
        if ($(window).scrollTop() > 0) {
          $block.removeClass("header-top-active");
        } else {
          $block.addClass("header-top-active");
        }
      });
      // Full Gnb
      $block.find(".btn-allmenu").on("click", function() {
        $block.find(".header-fullmenu").addClass("fullmenu-active");
      });
      $block.find(".fullmenu-close").on("click", function() {
        $block.find(".header-fullmenu").removeClass("fullmenu-active");
      });
      // Full Gnb DecoLine
      $block.find(".fullmenu-gnbitem").each(function() {
        const $this = $(this);
        $this.on("mouseover", function() {
          if (window.innerWidth > 992) {
            $this.find(".fullmenu-gnblink").addClass("on");
          }
        });
        $this.on("mouseout", function() {
          if (window.innerWidth > 992) {
            $this.find(".fullmenu-gnblink").removeClass("on");
          }
        });
      });
    });
  });
})();
/* tedu_light-N1 [EoM5c1Q6WN] */
(function() {
  $(function() {
    $(".tedu_light-N1[id=\'EoM5c1Q6WN\']").each(function() {
      const $block = $(this);
      // Header Mobile 1Depth Click
      if (window.innerWidth <= 992) {
        $block.find(".header-gnbitem").each(function() {
          const $gnblink = $(this).find(".header-gnblink");
          const $sublist = $(this).find(".header-sublist");
          if ($sublist.length) {
            $gnblink.attr("href", "javascript:void(0);");
          }
        });
      }
      // Header Top
      const $headerTop = $block.find(".header-top");
      const $gnbList = $block.find(".header-gnbitem");
      if ($headerTop.length && $gnbList.length) {
        $block.addClass("top-menu-active");
      }
      // Mobile Top
      $block.find(".btn-momenu").on("click", function() {
        $block.toggleClass("block-active");
        $block.find(".header-gnbitem").removeClass("item-active");
        $block.find(".header-sublist").removeAttr("style");
      });
      // Mobile Gnb
      function handleGnbEvents() {
        $block.find(".header-gnbitem").each(function() {
          const $this = $(this);
          const $thislink = $this.find(".header-gnblink");
          const $sublist = $this.find(".header-sublist");
          $thislink.off("click");
          if (window.innerWidth < 992) {
            $thislink.on("click", function() {
              const $clickedItem = $(this).parents(".header-gnbitem");
              if (!$clickedItem.hasClass("item-active")) {
                $block.find(".header-gnbitem").removeClass("item-active");
                $block.find(".header-sublist").stop().slideUp(300);
              }
              $clickedItem.toggleClass("item-active");
              $sublist.stop().slideToggle(300);
            });
          } else {
            $sublist.removeAttr("style");
            $block.removeClass("block-active");
            $block.find(".header-gnbitem").removeClass("item-active");
          }
        });
      }
      handleGnbEvents();
      $(window).on("resize", function() {
        handleGnbEvents();
      });
      //Scroll Header
      if ($(window).scrollTop() === 0) {
        $block.addClass("header-top-active");
      }
      $(window).scroll(function() {
        if ($(window).scrollTop() > 0) {
          $block.removeClass("header-top-active");
        } else {
          $block.addClass("header-top-active");
        }
      });
      // Full Gnb
      $block.find(".btn-allmenu").on("click", function() {
        $block.find(".header-fullmenu").addClass("fullmenu-active");
      });
      $block.find(".fullmenu-close").on("click", function() {
        $block.find(".header-fullmenu").removeClass("fullmenu-active");
      });
      // Full Gnb DecoLine
      $block.find(".fullmenu-gnbitem").each(function() {
        const $this = $(this);
        $this.on("mouseover", function() {
          if (window.innerWidth > 992) {
            $this.find(".fullmenu-gnblink").addClass("on");
          }
        });
        $this.on("mouseout", function() {
          if (window.innerWidth > 992) {
            $this.find(".fullmenu-gnblink").removeClass("on");
          }
        });
      });
    });
  });
})();
