import type { MenuModule } from '/@/router/types';
import { t } from '/@/hooks/web/useI18n';

const menu: MenuModule = {
  orderNo: 10,
  menu: {
    name: t('routes.dashboard.home'),
    path: '/dashboard',
    tag: {
      dot: false,
      type: 'warn',
    }
  },
};
export default menu;
