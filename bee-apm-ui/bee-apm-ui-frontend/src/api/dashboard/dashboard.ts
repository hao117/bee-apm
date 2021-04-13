import {apiHttp} from '/@/utils/http/axios';
import {
  SummaryResultModel
} from './model/dashboardModel';

enum ApiUrl {
  Summary = '/dashboard/summary',
}

/**
 * @description: summary api
 */
export function summaryApi() {
  return apiHttp.post<Array<SummaryResultModel>>(
    {
      url: ApiUrl.Summary,
    }
  );
}
