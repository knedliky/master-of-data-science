import numpy as np
import pandas as pd
from sklearn.base import BaseEstimator, TransformerMixin


class AverageTransformer(TransformerMixin, BaseEstimator):
    """
    Merges features by row-wise average to reduce features
    """

    def __init__(self, features, column_name=None):
        self.features = features
        self.column_name = column_name

    def fit(self, X, y=None):
        return self

    def transform(self, X, y=None):
        return np.expand_dims(X[self.features].mean(axis=1), axis=1)

    def get_feature_names_out(self, column_name):
        return self.column_name


class DateTransformer(TransformerMixin, BaseEstimator):
    """Transforms cyclical features such as hour, day, day_of_week, season"""

    def __init__(self, date):
        self.date = date

    def fit(self, X, y=None):
        return self

    def transform(self, X, y=None):
        result = pd.DataFrame(X, columns=self.date)
        result["date"] = pd.to_datetime(result["date"])
        result["hour"] = [dt.hour for dt in result["date"]]
        result["hour_sine"] = np.sin(2 * np.pi * result["hour"] / 24)
        result["hour_cos"] = np.cos(2 * np.pi * result["hour"] / 24)
        result["month"] = [dt.month for dt in result["date"]]
        result["month_sine"] = np.sin(2 * np.pi * result["month"] / 12)
        result["month_cos"] = np.cos(2 * np.pi * result["month"] / 12)
        result["weekend"] = [int(dt.weekday() > 4) for dt in result["date"]]
        result["season"] = [month % 12 // 3 + 1 for month in result["month"]]
        result["season_cos"] = -np.cos(2 * np.pi * result["season"] / 24)
        result["peak"] = [int(hour >= 7 and hour < 22) for hour in result["hour"]]

        return result[
            [
                "hour_sine",
                "hour_cos",
                "month_sine",
                "month_cos",
                "weekend",
                "season_cos",
                "peak",
            ]
        ]

    def get_feature_names_out(self, date):
        return [
            "hour_sine",
            "hour_cos",
            "month_sine",
            "month_cos",
            "weekend",
            "season_cos",
            "peak",
        ]


class CyclicalTransformer(TransformerMixin, BaseEstimator):
    """Transforms cyclical features such as hour, day, day_of_week, season"""

    def __init__(self, cyclical_features):
        self.cyclical_features = cyclical_features

    def fit(self, X, y=None):
        return self

    def transform(self, X, y=None):
        result = pd.DataFrame(X, columns=self.cyclical_features)
        result["date"] = pd.to_datetime(result["date"])
        result["hour"] = [dt.hour for dt in result["date"]]
        result["hour_sine"] = np.sin(2 * np.pi * result["hour"] / 24)
        result["hour_cos"] = np.cos(2 * np.pi * result["hour"] / 24)
        result["day"] = [dt.day for dt in result["date"]]
        result["day_sine"] = np.sin(2 * np.pi * result["day"] / 31)
        result["day_cos"] = np.cos(2 * np.pi * result["day"] / 31)
        result["month"] = [dt.month for dt in result["date"]]
        result["month_sine"] = np.sin(2 * np.pi * result["month"] / 12)
        result["month_cos"] = np.cos(2 * np.pi * result["month"] / 12)
        # result['year'] = [dt.year for dt in result['date']]
        result["day_of_week"] = [dt.weekday() for dt in result["date"]]
        result["dow_sine"] = np.sin(2 * np.pi * result["day_of_week"] / 7)
        result["dow_cos"] = np.cos(2 * np.pi * result["day_of_week"] / 7)
        result["weekend"] = [int(dt.weekday() > 4) for dt in result["date"]]
        result["season"] = [month % 12 // 3 + 1 for month in result["month"]]
        result["season_cos"] = -np.cos(2 * np.pi * result["season"] / 24)
        result["peak"] = [int(hour >= 7 and hour < 22) for hour in result["hour"]]

        return result[
            [
                "hour_sine",
                "hour_cos",
                "day_sine",
                "day_cos",
                "month_sine",
                "month_cos",
                "dow_sine",
                "dow_cos",
                "weekend",
                "season_cos",
                "peak",
            ]
        ]

    def get_feature_names_out(self, cyclical_features):
        return [
            "hour_sine",
            "hour_cos",
            "day_sine",
            "day_cos",
            "month_sine",
            "month_cos",
            "dow_sine",
            "dow_cos",
            "weekend",
            "season_cos",
            "peak",
        ]


class SimpleDateTransformer(TransformerMixin, BaseEstimator):
    """Transforms cyclical features such as hour, day, day_of_week, season"""

    def __init__(self, date):
        self.date = date

    def fit(self, X, y=None):
        return self

    def transform(self, X, y=None):
        result = pd.DataFrame(X, columns=self.date)
        result["date"] = pd.to_datetime(result["date"])
        result["hour"] = [dt.hour for dt in result["date"]]
        result["month"] = [dt.month for dt in result["date"]]
        result["weekend"] = [int(dt.weekday() > 4) for dt in result["date"]]
        result["season"] = [month % 12 // 3 + 1 for month in result["month"]]
        result["peak"] = [int(hour >= 7 and hour < 22) for hour in result["hour"]]

        return result[
            [
                "hour",
                "month",
                "weekend",
                "season",
                "peak",
            ]
        ]

    def get_feature_names_out(self, date):
        return [
            "hour",
            "month",
            "weekend",
            "season",
            "peak",
        ]
