import type { AppRouteModule } from '/@/router/types';

import { LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const dashboard: AppRouteModule = {
  path: '/dashboard',
  name: 'Dashboard',
  component: LAYOUT,
  redirect: '/dashboard/home',
  meta: {
    icon: 'ant-design:home-outlined',
    title: t('routes.dashboard.home'),
  },
  children: [
    {
      path: 'home',
      name: 'Home',
      component: () => import('/@/views/dashboard/home/index.vue'),
      meta: {
        affix: true,
        title: t('routes.dashboard.home'),
      },
    }
  ],
};

export default dashboard;
