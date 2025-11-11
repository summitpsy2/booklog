// === Theme Toggle ==========================================================
(function(){
  const key = 'theme';
  const root = document.documentElement;
  const btn = document.querySelector('[data-action="toggle-theme"]');

  // saved: 'dark' | 'light' | null
  const saved = localStorage.getItem(key);
  if(saved === 'dark' || saved === 'light') root.setAttribute('data-theme', saved);

  if(btn){
    btn.addEventListener('click', () => {
      const cur = root.getAttribute('data-theme') || 'light'; // 기본은 light
      const next = (cur === 'dark') ? 'light' : 'dark';
      root.setAttribute('data-theme', next);
      localStorage.setItem(key, next);
    });
  }
})();

// === Navbar Active Link ====================================================
(function(){
  const path = location.pathname;
  document.querySelectorAll('.navbar-nav .nav-link').forEach(a => {
    if(a.getAttribute('href') === path) a.classList.add('active');
  });
})();