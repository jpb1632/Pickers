/* campland-N1 */
(function() {
  $(function() {
    $(".campland-N1[id=\'Cxm5YnTQ2S\']").each(function() {
      const $block = $(this);
      // 네비게이션 아이템에서 서브메뉴가 있을 경우 링크의 href를 비활성화
      if (window.innerWidth <= 992) {
        $block.find(".header-gnbitem").each(function() {
          const $gnblink = $(this).find(".header-gnblink");
          const $sublist = $(this).find(".header-sublist");
          if ($sublist.length) {
            $gnblink.attr("href", "javascript:void(0);");
          }
        });
      }
      // 상단 메뉴 존재 여부 확인
      const $headerTop = $block.find(".header-top");
      const $gnbList = $block.find(".header-gnbitem");
      if ($headerTop.length && $gnbList.length) {
        $block.addClass("top-menu-active");
      }
      // 모바일 메뉴 버튼 클릭 시 네비게이션 메뉴 활성화/비활성화
      $block.find(".btn-momenu").on("click", function() {
        $block.toggleClass("block-active");
        $block.find(".header-gnbitem").removeClass("item-active");
        $block.find(".header-sublist").removeAttr("style");
      });
      // 네비게이션 메뉴 이벤트 처리 함수
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
      // 초기 실행 및 리사이즈 시 GNB 이벤트 적용
      handleGnbEvents();
      $(window).on("resize", function() {
        handleGnbEvents();
      });
      // 스크롤 위치가 최상단일 경우에만 상단 메뉴 활성화
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
      // 전체 메뉴 버튼 클릭 시 전체 메뉴 활성화
      $block.find(".btn-allmenu").on("click", function() {
        $block.find(".header-fullmenu").addClass("fullmenu-active");
      });
      // 전체 메뉴 닫기 버튼 클릭 시 전체 메뉴 비활성화
      $block.find(".fullmenu-close").on("click", function() {
        $block.find(".header-fullmenu").removeClass("fullmenu-active");
      });
      // 전체 메뉴 아이템 마우스 오버/아웃 이벤트 (큰 화면에서만)
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
