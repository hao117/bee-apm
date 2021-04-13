import type { MenuModule } from '/@/router/types';
import { t } from '/@/hooks/web/useI18n';

const menu: MenuModule = {
  orderNo: 10,
  menu: {
    name: t('routes.dashboard.dashboard'),
    path: '/dashboard',
    tag: {
      dot: false,
      type: 'warn',
    },
    children: [
      {
        path: 'analysis',
        name: t('routes.dashboard.analysis'),
        tag: {
          dot: false,
          type: 'warn',
        },
      }
    ],
  },
};
export default menu;
