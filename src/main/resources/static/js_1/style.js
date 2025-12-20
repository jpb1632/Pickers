/* basic-N3 */
(function() {
  $(function() {
    $(".basic-N3[id=\'FjM50KEki0\']").each(function() {
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

      // Mobile Lang
      $block.find(".header-langbtn").on("click", function() {
        $(this).parent().toggleClass("lang-active");
      });
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
/* basic-N50 */
(function() {
  $(function() {
    $(".basic-N50[id=\'EXM50KeknL\']").each(function() {
      const $block = $(this);
      const $dim = $block.find('.contents-dim');
      // Mobile Filter Open
      $block.find('.btn-filter').on('click', function() {
        $block.addClass('filter-active');
        $dim.fadeIn();
      });
      // Mobile Filter Close
      $block.find('.btn-close, .contents-dim').on('click', function() {
        $block.removeClass('filter-active');
        $dim.fadeOut();
      });
    });
  });
})();
/* hooms-N2 */
(function() {
  $(function() {
    $(".hooms-N2[id=\'oVM50kS6FA\']").each(function() {
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
/* hooms-N37 */
(function() {
  $(function() {
    $(".hooms-N37[id=\'rFM50KS6j4\']").each(function() {
      const $block = $(this);
      const thumbnail = $block.find(".contents-thumbnail img");
      const thumbitem = $block.find(".contents-thumbitem");
      // 메인 이미지 변경
      thumbitem.click(function() {
        const idx = $(this).index();
        console.log(idx);
        thumbnail.removeClass("active").eq(idx).addClass("active");
      });
    });
  });
})();
/* portcard-N1 */
(function() {
  $(function() {
    $(".portcard-N1[id=\'nxM50Li2n5\']").each(function() {
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
      // Scroll Header
      $(window).scroll(function() {
        if ($(window).scrollTop() > 0) {
          $block.addClass("header-top-active");
        } else {
          $block.removeClass("header-top-active");
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
/* portcard-N4 */
(function() {
  $(function() {
    $(".portcard-N4[id=\'dMm50li2tD\']").each(function() {
      const $block = $(this);
      // Swiper
      const mainVisualSwiper = new Swiper(".portcard-N4[id=\'dMm50li2tD\'] .visual-swiper", {
        slidesPerView: 1,
        speed: 600,
        loop: true,
        pagination: {
          el: ".portcard-N4[id=\'dMm50li2tD\'] .swiper-pagination",
          clickable: "true",
        },
        breakpoints: {
          992: {
            slidesPerView: 2,
          },
          1200: {
            slidesPerView: 3,
          },
        },
      });

      // Swiper Control Button
      mainVisualSwiper.on("slideChange", function() {
        var getAllIndex = this.snapGrid.length - 1;
        if (mainVisualSwiper.activeIndex == getAllIndex) {
          $block.find(".swiper-paging-btn").removeClass("next");
          $block.find(".swiper-paging-btn").addClass("back");
        } else {
          $block.find(".swiper-paging-btn").removeClass("back");
          $block.find(".swiper-paging-btn").addClass("next");
        }
      });
      $block.find(".swiper-paging-btn").on("click", function() {
        if ($(this).hasClass("next")) {
          mainVisualSwiper.slideNext(800);
        } else {
          mainVisualSwiper.slidePrev(800);
        }
      });
    });
  });
})();
/* portcard-N7 */
(function() {
  $(function() {
    $(".portcard-N7[id=\'ftM50li34H\']").each(function() {
      const $block = $(this);
      // Swiper
      const swiper = new Swiper(".portcard-N7[id=\'ftM50li34H\'] .visual-swiper", {
        slidesPerView: 1.1,
        spaceBetween: 20,
        speed: 600,
        loop: true,
        navigation: {
          nextEl: ".portcard-N7[id=\'ftM50li34H\'] .swiper-button-next",
          prevEl: ".portcard-N7[id=\'ftM50li34H\'] .swiper-button-prev",
        },
        pagination: {
          el: ".portcard-N7[id=\'ftM50li34H\'] .swiper-pagination",
          type: "progressbar",
        },
        on: {
          init: function() {
            const totalSlides = this.slides.filter(
              (slide) => !slide.classList.contains("swiper-slide-duplicate")
            ).length;
            $block
              .find(".swiper-paging-total")
              .text(totalSlides.toString().padStart(2, "0"));
          },
          slideChange: function() {
            const currentIndex = this.realIndex + 1;
            $block
              .find(".swiper-paging-current")
              .text(currentIndex.toString().padStart(2, "0"));
          },
        },
        breakpoints: {
          992: {
            slidesPerView: 2,
          },
          1024: {
            slidesPerView: 2.54,
            spaceBetween: 40,
          },
        },
      });
    });
  });
})();
/* hospital2-N2 */
(function() {
  $(function() {
    $(".hospital2-N2[id=\'SPM50LMZwn\']").each(function() {
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
/* hospital2-N5 */
(function() {
  $(function() {
    $(".hospital2-N5[id=\'qfM50lMZZq\']").each(function() {
      const $block = $(this);
      // Swiper
      const swiper = new Swiper(".hospital2-N5[id=\'qfM50lMZZq\'] .swiper", {
        loop: true,
        slidesPerView: 1,
        autoplay: {
          delay: 5000,
        },
        navigation: {
          nextEl: ".hospital2-N5[id=\'qfM50lMZZq\'] .btn-next",
          prevEl: ".hospital2-N5[id=\'qfM50lMZZq\'] .btn-prev",
        },
        pagination: {
          el: ".hospital2-N5[id=\'qfM50lMZZq\'] .swiper-pagination",
          type: "progressbar",
        },
        breakpoints: {
          992: {
            threshold: 100,
          },
        },
        on: {
          init: function() {
            const totalSlides = this.slides.filter(
              (slide) => !slide.classList.contains("swiper-slide-duplicate")
            ).length;
            $block.find(".total").text(totalSlides);
          },
          slideChange: function() {
            $block.find(".curr").text(this.realIndex + 1);
          },
        },
      });
      // 정지
      $block.find(".pause").click(function() {
        swiper.autoplay.stop();
        $(this).removeClass("active");
        $(this).siblings().addClass("active");
      });
      // 재생
      $block.find(".play").click(function() {
        swiper.autoplay.start();
        $(this).removeClass("active");
        $(this).siblings().addClass("active");
      });
    });
  });
})();
/* hospital2-N14 */
(function() {
  $(function() {
    $(".hospital2-N14[id=\'Pqm50ln07u\']").each(function() {
      const $block = $(this);
      const $contentsContainer = $block.find(".contents-container");
      const $listArea = $block.find(".list-area");
      let isMobileOrSmEntered = false;
      let isDesktopEntered = false;
      // 아이템 개수에 따라 grid-template-columns 동적으로 설정
      function updateGridColumns() {
        const $items = $block.find(".item");
        const itemCount = $items.length;
        const columns =
          itemCount <= 3 ? 3 : itemCount <= 5 ? 4 : itemCount <= 7 ? 5 : 6;
        $listArea.css(
          "grid-template-columns",
          `repeat(${columns}, minmax(0, 60rem))`
        );
      }
      // 아이템 순서 재정렬 함수
      function reorderItems() {
        $block.find(".item").each(function(index) {
          $(this).css("order", index);
        });
      }
      // 클릭 이벤트 처리
      function itemClickHandler() {
        const $this = $(this);
        const $items = $block.find(".item");
        const isSmContainer = $block
          .find(".contents-container")
          .hasClass("container-sm");
        if (window.innerWidth <= 992 || isSmContainer) {
          handleMobileBehavior($items, $this);
        } else {
          const idx = $this.index();
          const itemCount = $items.length;
          const columns =
            itemCount <= 3 ? 3 : itemCount <= 5 ? 4 : itemCount <= 7 ? 5 : 6;
          $items.removeClass("active").removeAttr("style");
          $this.addClass("active");
          const startIndex =
            columns === 6 ? 5 : columns === 5 ? 4 : columns === 4 ? 3 : 2;
          if (idx >= startIndex) {
            const colStart = idx - startIndex + 1;
            const colEnd = colStart + 2;
            const rowStart = 1;
            const rowEnd = rowStart + 2;
            $this.css({
              gridColumn: `${colStart} / ${colEnd}`,
              gridRow: `${rowStart} / ${rowEnd}`,
            });
          }
        }
      }
      // 모바일 및 .container-sm 환경 처리
      function handleMobileBehavior($items, $clickedItem) {
        $items.removeClass("active").removeAttr("style");
        $clickedItem.addClass("active");
        const idx = $clickedItem.index();
        if (idx % 2 !== 0) {
          const $nextItem = $items.eq(idx + 1);
          if ($nextItem.length > 0) {
            $nextItem.css({
              gridRow: Math.floor(idx / 2) + 1,
              gridColumn: 2,
            });
          }
        }
      }
      // 윈도우 크기 변경 및 클래스 변경 시 처리
      function handleWindowResizeOrClassChange() {
        const $items = $block.find(".item");
        $items.removeAttr("style");
        $items.removeClass("active").first().addClass("active");
        $items.off("click").on("click", itemClickHandler);
        updateGridColumns();
      }
      // 화면 크기 변경 감지
      function handleResize() {
        const currentWidth = window.innerWidth;
        if (currentWidth <= 992 && !isMobileOrSmEntered) {
          isMobileOrSmEntered = true;
          isDesktopEntered = false;
          handleWindowResizeOrClassChange();
        } else if (currentWidth > 992 && !isDesktopEntered) {
          isDesktopEntered = true;
          isMobileOrSmEntered = false;
          handleWindowResizeOrClassChange();
        }
      }
      // DOM 변경 감시 (아이템 삭제 시)
      const itemObserver = new MutationObserver((mutations) => {
        mutations.forEach((mutation) => {
          if (mutation.type === "childList") {
            updateGridColumns();
          }
        });
      });
      // contents-container 변경 감시 (클래스 변경 등)
      const containerObserver = new MutationObserver(() => {
        handleWindowResizeOrClassChange();
      });
      // list-area의 자식 요소 변경 감시
      itemObserver.observe($listArea[0], {
        childList: true,
      });
      // contents-container의 클래스 변경 감시
      containerObserver.observe($contentsContainer[0], {
        attributes: true,
        attributeFilter: ["class"],
      });
      // 초기화
      reorderItems();
      updateGridColumns();
      handleResize();
      // 리사이즈 이벤트 처리
      $(window).resize(handleResize);
    });
  });
})();
/* hospital2-N15 */
(function() {
  $(function() {
    $(".hospital2-N15[id=\'HuM50LN0Bd\']").each(function() {
      const $block = $(this);
      // Swiper
      const swiper = new Swiper(".hospital2-N15[id=\'HuM50LN0Bd\'] .swiper", {
        loop: true,
        slidesPerView: 1,
        spaceBetween: 16,
        autoplay: {
          delay: 5000,
        },
        navigation: {
          nextEl: ".hospital2-N15[id=\'HuM50LN0Bd\'] .btn-next",
          prevEl: ".hospital2-N15[id=\'HuM50LN0Bd\'] .btn-prev",
        },
        pagination: {
          el: ".hospital2-N15[id=\'HuM50LN0Bd\'] .paging",
          type: "bullets",
          clickable: true,
        },
        breakpoints: {
          1200: {
            slidesPerView: 3,
            spaceBetween: 20,
          },
          768: {
            slidesPerView: 2,
            spaceBetween: 16,
          },
        },
      });
    });
  });
})();
/* hospital2-N16 */
(function() {
  $(function() {
    $(".hospital2-N16[id=\'BmM50lN0go\']").each(function() {
      const $block = $(this);
      const $contentsContainer = $block.find(".contents-container");
      // Swiper 초기화 함수
      function initSwiper() {
        if (window.swiperInstance) {
          window.swiperInstance.destroy();
        }
        const isSmContainer = $contentsContainer.hasClass("container-sm");
        window.swiperInstance = new Swiper(".hospital2-N16[id=\'BmM50lN0go\'] .swiper", {
          loop: true,
          slidesPerView: "auto",
          spaceBetween: 16,
          autoplay: {
            delay: 5000,
          },
          navigation: {
            nextEl: ".hospital2-N16[id=\'BmM50lN0go\'] .btn-next",
            prevEl: ".hospital2-N16[id=\'BmM50lN0go\'] .btn-prev",
          },
          pagination: {
            el: ".hospital2-N16[id=\'BmM50lN0go\'] .paging",
            type: "bullets",
            clickable: true,
          },
          breakpoints: {
            1400: {
              slidesPerView: isSmContainer ? 3 : 4,
              spaceBetween: 20,
            },
            992: {
              spaceBetween: 20,
              slidesPerView: isSmContainer ? 3 : 3,
            }
          },
        });
      }
      // DOM 변경 감시
      const observer = new MutationObserver((mutations) => {
        mutations.forEach((mutation) => {
          if (mutation.type === 'attributes' && mutation.attributeName === 'class') {
            initSwiper();
          }
        });
      });
      // contents-container의 클래스 변경 감시
      observer.observe($contentsContainer[0], {
        attributes: true,
        attributeFilter: ["class"]
      });
      // 초기 Swiper 초기화
      initSwiper();
    });
  });
})();
/* hospital2-N18 */
(function() {
  $(function() {
    $(".hospital2-N18[id=\'bvm50ln0NL\']").each(function() {
      const $block = $(this);
      // Swiper
      const swiper = new Swiper(".hospital2-N18[id=\'bvm50ln0NL\'] .swiper", {
        loop: true,
        slidesPerView: "auto",
        spaceBetween: 16,
        speed: 10000,
        allowTouchMove: false,
        autoplay: {
          delay: 0,
          disableOnInteraction: false,
        },
        breakpoints: {
          992: {
            spaceBetween: 40,
          },
        },
      });
    });
  });
})();
/* opilsol-N1 [fWm50mE9Tw] */
(function() {
  $(function() {
    $(".opilsol-N1[id=\'fWm50mE9Tw\']").each(function() {
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
      const $headerTop = $block.find(".header-top");
      const $gnbList = $block.find(".header-gnbitem");
      if ($headerTop.length && $gnbList.length) {
        $block.addClass("top-menu-active");
      }
      // Mobile Lang
      $block.find(".header-langbtn").on("click", function() {
        $(this).parent().toggleClass("lang-active");
      });
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
      //Gnb Hover
      // GNB 전체 영역에 대한 호버 이벤트 처리
      $block.find(".header-gnb").hover(
        function() {
          $block.addClass("gnb-active");
        },
        function() {
          $block.removeClass("gnb-active");
        }
      );
    });
  });
})();
/* opilsol-N1 [boM50mFB7r] */
(function() {
  $(function() {
    $(".opilsol-N1[id=\'boM50mFB7r\']").each(function() {
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
      const $headerTop = $block.find(".header-top");
      const $gnbList = $block.find(".header-gnbitem");
      if ($headerTop.length && $gnbList.length) {
        $block.addClass("top-menu-active");
      }
      // Mobile Lang
      $block.find(".header-langbtn").on("click", function() {
        $(this).parent().toggleClass("lang-active");
      });
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
      //Gnb Hover
      // GNB 전체 영역에 대한 호버 이벤트 처리
      $block.find(".header-gnb").hover(
        function() {
          $block.addClass("gnb-active");
        },
        function() {
          $block.removeClass("gnb-active");
        }
      );
    });
  });
})();
