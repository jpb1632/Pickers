// tedu-N3 [XEm3ZgHi4n]
(function() {
  $(function() {
    $(".tedu-N3").each(function() {
      const $block = $(this);
      // Lang
      $block.find(".header-lang").on("click", function() {
        const $this = $(this);
        $this.toggleClass("lang-active");
      });
      // Condition
      const hdArea = $block.find(".header-area");
      const hdSearch = $block.find(".header-search");
      // Menu
      $block.find(".menu-open").on("click", function() {
        const $this = $(this);
        if (hdSearch.length > 0) {
          hdSearch.removeClass("is-active");
        }
        if (window.innerWidth <= 980) {
          $block.find(".header-container").addClass("mo-active");
          $this.hide();
          $block.find(".menu-close").show();
        } else {
          $block.find(".header-area").addClass("is-active");
          $this.hide();
          $block.find(".menu-close").show();
        }
      });
      $block.find(".menu-close").on("click", function() {
        const $this = $(this);
        if (window.innerWidth <= 980) {
          $block.find(".header-container").removeClass("mo-active");
          $this.hide();
          $block.find(".menu-open").show();
        } else {
          $block.find(".header-area").removeClass("is-active");
          $this.hide();
          $block.find(".menu-open").show();
        }
      });
      // Search
      $block.find(".search-btn").on("click", function() {
        $(this).hide();
        $block.find(".search-close").show();
        if (hdArea.length > 0) {
          hdArea.removeClass("is-active");
          $block.find(".menu-open").show();
          $block.find(".menu-close").hide();
        }
        $block.find(".header-search").addClass("is-active");
      });
      $block.find(".search-close").on("click", function() {
        $(this).hide();
        $block.find(".search-btn").show();
        $block.find(".header-search").removeClass("is-active");
      });
      // Mobile Menu
      $block.find(".mhd-menu-box").each(function() {
        const $this = $(this);
        const $thislink = $this.find(".mhd-menu-title");
        $thislink.on("click", function() {
          if (!$(this).parent().hasClass("is-active")) {
            $(".mhd-menu-box").removeClass("is-active");
          }
          $(this).parents(".mhd-menu-box").toggleClass("is-active");
        });
      });
      // Mobile Area
      $(window).on("resize", function() {
        if (window.innerWidth >= 980) {
          $block.find(".header-container").removeClass("mo-active");
          $block.find(".menu-close").hide();
          $block.find(".menu-open").show();
        } else if (window.innerWidth <= 980) {
          $block.find(".header-area").removeClass("is-active");
          $block.find(".menu-close").hide();
          $block.find(".menu-open").show();
          $block.find(".header-lang").removeClass("lang-active");
          $block.find(".header-search").removeClass("is-active");
        }
      });
    });
  });
})();
// tedu-N17 [fLM3zgHIUl]
(function() {
  $(function() {
    $(".tedu-N17").each(function() {
      const $block = $(this);
      // Site
      $block.find(".site-wrap").on("click", function() {
        const $this = $(this);
        $this.toggleClass("site-active");
      });
      // Mobile Area
      $(window).on('resize', function() {
        if (window.innerWidth <= 980) {
          $block.find(".site-wrap").removeClass("site-active");
        }
      });
    });
  });
})();